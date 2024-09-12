package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.WorkingHoursCreateDTO;
import prod.bookapp.entity.WorkingHours;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkingHoursViewDTOConverter {
    public WorkingHoursCreateDTO convertToCreateDTO(WorkingHours workingHours) {
        WorkingHoursCreateDTO workingHoursCreateDTO = new WorkingHoursCreateDTO();
        workingHoursCreateDTO.setStartTime(workingHours.getStartTime());
        workingHoursCreateDTO.setEndTime(workingHours.getEndTime());
        workingHoursCreateDTO.setDayOfWeek(workingHours.getDayOfWeek());
        return workingHoursCreateDTO;
    }
    public List<WorkingHoursCreateDTO> convertToCreateDTO(List<WorkingHours> workingHours){
        List<WorkingHoursCreateDTO> workingHoursCreateDTOList = new ArrayList<>();
        for (WorkingHours wh : workingHours) {
            workingHoursCreateDTOList.add(this.convertToCreateDTO(wh));
        }
        return workingHoursCreateDTOList;
    }
}
