package prod.bookapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prod.bookapp.dto.WorkingHoursCreateDTO;
import prod.bookapp.dto.WorkingHoursViewDTO;
import prod.bookapp.dto.converter.WorkingHoursViewDTOConverter;
import prod.bookapp.entity.User;
import prod.bookapp.entity.WorkingHours;
import prod.bookapp.repository.WorkingHoursRepository;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;
    private final WorkingHoursViewDTOConverter workingHoursViewDTOConverter;

    public WorkingHoursService(WorkingHoursRepository workingHoursRepository, WorkingHoursViewDTOConverter workingHoursViewDTOConverter) {
        this.workingHoursRepository = workingHoursRepository;
        this.workingHoursViewDTOConverter = workingHoursViewDTOConverter;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    private Set<Integer> findDuplicatesByDayOfWeek(List<WorkingHoursCreateDTO> list) {
        return list.stream()
                .collect(Collectors.groupingBy(WorkingHoursCreateDTO::getDayOfWeek, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private boolean hasTimeIssue(List<WorkingHoursCreateDTO> list) {
        return list.stream().anyMatch(x -> x.getStartTime().isAfter(x.getEndTime()) || x.getEndTime().equals(x.getStartTime()));
    }

    String validateWH(List<WorkingHoursCreateDTO> whDTOList) {
        if (hasTimeIssue(whDTOList)) {
            return "Error: Sorry, but you have time issues";
        }
        if (!findDuplicatesByDayOfWeek(whDTOList).isEmpty()) {
            return "Error: Sorry, but you have duplicate day";
        }
        return null;
    }

    List<WorkingHours> getAllByOwner(User owner) {
        return workingHoursRepository.findAllByOwner(owner);
    }

    void deleteAllByOwner(User owner) {
        workingHoursRepository.deleteAllByOwner(owner);
    }

    void saveAll(List<WorkingHours> workingHoursList) {
        workingHoursRepository.saveAll(workingHoursList);
    }

    @Transactional
    public String createOrUpdate(List<WorkingHoursCreateDTO> whDTOList, Authentication authentication) {
        var validationResult = validateWH(whDTOList);
        if (validationResult != null) {
            return validationResult;
        }
        User owner = getAuthUser(authentication);
        List<WorkingHours> existedWH = getAllByOwner(owner);
        if (!existedWH.isEmpty()) {
            deleteAllByOwner(owner);
        }
        List<WorkingHours> whList = new ArrayList<>();
        for (WorkingHoursCreateDTO whDTO : whDTOList) {
            WorkingHours workingHours = new WorkingHours();
            workingHours.setOwner(owner);
            workingHours.setDayOfWeek(whDTO.getDayOfWeek());
            workingHours.setStartTime(whDTO.getStartTime());
            workingHours.setEndTime(whDTO.getEndTime());
            whList.add(workingHours);
        }

        saveAll(whList);
        return whList.stream().map(l -> l.getId().toString()).toList().toString();
    }


    public WorkingHours findByOwnerAndDayOfWeek(User user, DayOfWeek dayOfWeek) {
        return workingHoursRepository.findByOwnerAndDayOfWeek(user, dayOfWeek.getValue()).orElse(null);
    }

    public List<WorkingHoursViewDTO> getAll(Authentication authentication) {
        User owner = getAuthUser(authentication);
        return workingHoursViewDTOConverter.convertToViewDTO(workingHoursRepository.findAllByOwner(owner));
    }


}
