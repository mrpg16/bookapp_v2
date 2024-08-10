package prod.bookapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prod.bookapp.dto.WorkingHoursCreateDTO;
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

    public WorkingHoursService(WorkingHoursRepository workingHoursRepository) {
        this.workingHoursRepository = workingHoursRepository;
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

    @Transactional
    public String createOrUpdate(List<WorkingHoursCreateDTO> whDTOList, Authentication authentication) {
        if (hasTimeIssue(whDTOList)) {
            return "Sorry, but you have time issues";
        }
        if (!findDuplicatesByDayOfWeek(whDTOList).isEmpty()) {
            return "Sorry, but you have duplicate day";
        }
        User owner = getAuthUser(authentication);
        List<WorkingHours> existedWH = workingHoursRepository.findByOwner(owner);
        if (!existedWH.isEmpty()) {
            workingHoursRepository.deleteAllByOwner(owner);
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

        workingHoursRepository.saveAll(whList);
        return whList.stream().map(l -> l.getId().toString()).toList().toString();
    }


    public WorkingHours findByOwnerAndDayOfWeek(User user, DayOfWeek dayOfWeek) {
        return workingHoursRepository.findByOwnerAndDayOfWeek(user, dayOfWeek.getValue());
    }


}
