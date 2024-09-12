package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.WorkingHoursViewDTO;
import prod.bookapp.entity.WorkingHours;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkingHoursViewDTOConverter {
    public WorkingHoursViewDTO convertToViewDTO(WorkingHours workingHours) {
        WorkingHoursViewDTO workingHoursViewDTO = new WorkingHoursViewDTO();
        workingHoursViewDTO.setId(workingHours.getId());
        workingHoursViewDTO.setStartTime(workingHours.getStartTime());
        workingHoursViewDTO.setEndTime(workingHours.getEndTime());
        workingHoursViewDTO.setDayOfWeek(workingHours.getDayOfWeek());
        return workingHoursViewDTO;
    }
    public List<WorkingHoursViewDTO> convertToViewDTO(List<WorkingHours> workingHours){
        List<WorkingHoursViewDTO> workingHoursViewDTOList = new ArrayList<>();
        for (WorkingHours wh : workingHours) {
            workingHoursViewDTOList.add(this.convertToViewDTO(wh));
        }
        return workingHoursViewDTOList;
    }
}
