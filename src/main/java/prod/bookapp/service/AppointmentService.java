package prod.bookapp.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.AppointmentViewWorkerDTO;
import prod.bookapp.dto.converter.AppointmentViewDTOConverter;
import prod.bookapp.entity.Appointment;
import prod.bookapp.entity.User;
import prod.bookapp.enums.Enums;
import prod.bookapp.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentViewDTOConverter appointmentViewDTOConverter;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentViewDTOConverter appointmentViewDTOConverter) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentViewDTOConverter = appointmentViewDTOConverter;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    private List<AppointmentViewWorkerDTO> getAppointmentViewWorkerDTOS(List<Appointment> appointmentList) {
        List<AppointmentViewWorkerDTO> dtos = new ArrayList<>();
        if (appointmentList != null && !appointmentList.isEmpty()) {
            for (Appointment app : appointmentList) {
                dtos.add(appointmentViewDTOConverter.convertToAppointmentViewWorkerDTO(app));
            }
        }
        dtos.sort(Comparator.comparing(AppointmentViewWorkerDTO::getDate).thenComparing(AppointmentViewWorkerDTO::getTimeStart));
        return dtos;
    }

    public List<Appointment> getAllAppointmentsByWorkerAndDate(User worker, LocalDate date) {
        return appointmentRepository.findAllByWorkerAndDate(worker, date);
    }

    public List<Appointment> getAllAppointmentsByWorkerAndDateAndStatusIsNot(User worker, LocalDate date, String status) {
        return appointmentRepository.findAllByWorkerAndDateAndStatusIsNot(worker, date, status);
    }

    public List<Appointment> getAllAppointmentsByClientAndDate(User client, LocalDate date) {
        return appointmentRepository.findAllByClientAndDate(client, date);
    }

    boolean hasAppointment(LocalDate date, LocalTime timeStart, User user) {
        List<Appointment> appsForDay = appointmentRepository.findAllByClientAndDate(user, date);

        Appointment app = appsForDay.stream()
                .filter(x -> x.getTimeStart().isBefore(timeStart) || x.getTimeStart().equals(timeStart))
                .filter(x -> x.getTimeEnd().isAfter(timeStart) || x.getTimeEnd().equals(timeStart))
                .findFirst().orElse(null);
        return app != null;
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }


    public List<AppointmentViewWorkerDTO> getAppointmentsByWorkerAndDateBetween(Authentication authentication, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return null;
        }
        List<Appointment> appointmentList;
        if (!start.equals(end)) {
            appointmentList = appointmentRepository.findAllByWorkerAndDateBetween(getAuthUser(authentication), start, end);
        } else {
            appointmentList = getAllAppointmentsByWorkerAndDate(getAuthUser(authentication), start);
        }
        return getAppointmentViewWorkerDTOS(appointmentList);
    }

    public List<AppointmentViewWorkerDTO> getAppointmentsByClientAndDateBetween(Authentication authentication, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return null;
        }
        List<Appointment> appointmentList;
        if (!start.equals(end)) {
            appointmentList = appointmentRepository.findAllByClientAndDateBetween(getAuthUser(authentication), start, end);
        } else {
            appointmentList = getAllAppointmentsByClientAndDate(getAuthUser(authentication), start);
        }
        return getAppointmentViewWorkerDTOS(appointmentList);
    }

    @Transactional
    public String confirm(Long id, Authentication authentication){
        Appointment app = appointmentRepository.findByIdAndWorkerAndStatus(id, getAuthUser(authentication), Enums.APPOINTMENT_STATUS_UNCONFIRMED.getValue());
        if(app == null){
            return "Not found";
        }
        app.setStatus(Enums.APPOINTMENT_STATUS_CONFIRMED.getValue());
        appointmentRepository.save(app);
        return app.getId().toString();
    }

    @Transactional
    public String reject(Long id, Authentication authentication){
        Appointment app = appointmentRepository.findByIdAndWorkerAndStatus(id, getAuthUser(authentication), Enums.APPOINTMENT_STATUS_UNCONFIRMED.getValue());
        if(app == null){
            return "Not found";
        }
        app.setStatus(Enums.APPOINTMENT_STATUS_REJECTED.getValue());
        appointmentRepository.save(app);
        return app.getId().toString();
    }


}
