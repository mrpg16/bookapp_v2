package prod.bookapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import prod.bookapp.dto.TimeSlotDTO;
import prod.bookapp.dto.converter.AppointmentViewDTOConverter;
import prod.bookapp.entity.Appointment;
import prod.bookapp.entity.Proposal;
import prod.bookapp.entity.User;
import prod.bookapp.entity.WorkingHours;
import prod.bookapp.enums.Enums;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class TimeSlotService {
    private final WorkingHoursService workingHoursService;
    private final AppointmentService appointmentService;
    private final AppointmentViewDTOConverter appointmentViewDTOConverter;
    private final UserService userService;
    private final ProposalService proposalService;

    public TimeSlotService(WorkingHoursService workingHoursService, AppointmentService appointmentService, AppointmentViewDTOConverter appointmentViewDTOConverter, UserService userService, ProposalService proposalService) {
        this.workingHoursService = workingHoursService;
        this.appointmentService = appointmentService;
        this.appointmentViewDTOConverter = appointmentViewDTOConverter;
        this.userService = userService;
        this.proposalService = proposalService;
    }

    private User getAuthUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    private List<TimeSlotDTO> freeSlotFromWorkingHours(WorkingHours wh, LocalDate date) {
        TimeSlotDTO freeSlot = new TimeSlotDTO();
        freeSlot.setStartTime(wh.getStartTime());
        freeSlot.setEndTime(wh.getEndTime());
        freeSlot.setType(Enums.SLOT_TYPE_FREE.getValue());
        freeSlot.setDate(date);
        freeSlot.setAppointment(null);
        freeSlot.setDurationMin((int) Duration.between(wh.getStartTime(), wh.getEndTime()).toMinutes());
        return Collections.singletonList(freeSlot);
    }

    public List<TimeSlotDTO> getAllTimeSlotsByDateAndWorker(LocalDate date, User worker) {
        WorkingHours wh = workingHoursService.findByOwnerAndDayOfWeek(worker, date.getDayOfWeek());
        if (wh == null) {
            return null;
        }
        List<TimeSlotDTO> slots = new ArrayList<>();
        List<Appointment> apps = appointmentService.getAllAppointmentsByWorkerAndDateAndStatusIsNot(worker, date, Enums.APPOINTMENT_STATUS_REJECTED.getValue());
        if (apps == null) {
            return freeSlotFromWorkingHours(wh, date);
        }
        if (apps.isEmpty()) {
            return freeSlotFromWorkingHours(wh, date);
        }
        apps.sort(Comparator.comparing(Appointment::getTimeStart));
        LocalTime currentStart = wh.getStartTime();
        for (Appointment app : apps) {
            if (currentStart.isBefore(app.getTimeStart())) {
                TimeSlotDTO freeSlot = new TimeSlotDTO();
                freeSlot.setStartTime(currentStart);
                freeSlot.setEndTime(app.getTimeStart());
                freeSlot.setType(Enums.SLOT_TYPE_FREE.getValue());
                freeSlot.setDate(date);
                freeSlot.setAppointment(null);
                freeSlot.setDurationMin((int) Duration.between(currentStart, app.getTimeStart()).toMinutes());
                slots.add(freeSlot);
            }
            TimeSlotDTO busySlot = new TimeSlotDTO();
            busySlot.setStartTime(app.getTimeStart());
            busySlot.setEndTime(app.getTimeEnd());
            busySlot.setType(Enums.SLOT_TYPE_BUSY.getValue());
            busySlot.setDate(date);
            busySlot.setAppointment(appointmentViewDTOConverter.convertToAppointmentViewDTO(app));
            busySlot.setDurationMin((int) Duration.between(app.getTimeStart(), app.getTimeEnd()).toMinutes());

            slots.add(busySlot);
            currentStart = app.getTimeEnd();
        }

        if (currentStart.isBefore(wh.getEndTime())) {
            TimeSlotDTO freeSlot = new TimeSlotDTO();
            freeSlot.setStartTime(currentStart);
            freeSlot.setEndTime(wh.getEndTime());
            freeSlot.setType(Enums.SLOT_TYPE_FREE.getValue());
            freeSlot.setDate(date);
            freeSlot.setAppointment(null);
            freeSlot.setDurationMin((int) Duration.between(currentStart, wh.getEndTime()).toMinutes());
            slots.add(freeSlot);
        }
        return slots;
    }

    public List<TimeSlotDTO> getAllTimeSlotsByDateBetweenAndWorker(LocalDate dateFrom, LocalDate dateTo, User worker) {
        if (dateFrom.isAfter(dateTo)) {
            return null;
        }
        List<LocalDate> dates = dateFrom.datesUntil(dateTo).toList();
        List<TimeSlotDTO> slots = new ArrayList<>();
        for (LocalDate date : dates) {
            List<TimeSlotDTO> slotsByDay = getAllTimeSlotsByDateAndWorker(date, worker);
            if (slotsByDay != null) {
                slots.addAll(slotsByDay);
            }
        }
        return slots;
    }

    public List<TimeSlotDTO> getAllFreeSlotsByDateAndWorkerAndDuration(LocalDate date, User worker, int durationMin) {
        List<TimeSlotDTO> allSlots = getAllTimeSlotsByDateAndWorker(date, worker);
        if (allSlots == null) {
            return null;
        }
        return allSlots.stream()
                .filter(x -> Objects.equals(x.getType(), Enums.SLOT_TYPE_FREE.getValue()))
                .filter(x -> x.getDurationMin() >= durationMin).toList();

    }


    public List<TimeSlotDTO> getAllFreeSlotsByDateBetweenAndWorkerAndDuration(LocalDate dateFrom, LocalDate dateTo, User worker, int durationMin) {
        List<TimeSlotDTO> allSlots = getAllTimeSlotsByDateBetweenAndWorker(dateFrom, dateTo, worker);
        if (allSlots == null) {
            return null;
        }
        return allSlots.stream()
                .filter(x -> Objects.equals(x.getType(), Enums.SLOT_TYPE_FREE.getValue()))
                .filter(x -> x.getDurationMin() >= durationMin).toList();

    }


    public List<TimeSlotDTO> getAllFreeSlotsByDateAndWorkerIdAndProposalId(LocalDate date, Long workerId, Long proposalId) {
        User worker = userService.getUserById(workerId);
        Proposal proposal = proposalService.getProposalByIdAndOwner(proposalId, worker);
        if (proposal == null) {
            return null;
        }
        return getAllFreeSlotsByDateAndWorkerAndDuration(date, worker, proposal.getDurationMin());
    }

    public List<TimeSlotDTO> getAllFreeSlotsByDateBetweenAndWorkerIdAndProposalId(LocalDate dateFrom, LocalDate dateTo, Long workerId, Long proposalId) {
        User worker = userService.getUserById(workerId);
        Proposal proposal = proposalService.getProposalByIdAndOwner(proposalId, worker);
        if (proposal == null) {
            return null;
        }
        return getAllFreeSlotsByDateBetweenAndWorkerAndDuration(dateFrom, dateTo, worker, proposal.getDurationMin());
    }

    public TimeSlotDTO getFreeSlotByDateTimeAndProposalIdAndWorkerId(LocalDate date, LocalTime timeStart, Long proposalId, Long workerId) { //validation for appointment
        User worker = userService.getUserById(workerId);
        Proposal proposal = proposalService.getProposalByIdAndOwner(proposalId, worker);
        if (proposal == null) {
            return null;
        }
        List<TimeSlotDTO> freeSlotsByDate = getAllFreeSlotsByDateAndWorkerIdAndProposalId(date, workerId, proposalId);
        if (freeSlotsByDate == null) {
            return null;
        }

        LocalTime timeEnd = timeStart.plusMinutes(proposal.getDurationMin());
        return freeSlotsByDate.stream()
                .filter(x -> timeStart.isAfter(x.getStartTime()) || timeStart.equals(x.getStartTime()))
                .filter(x -> timeEnd.isBefore(x.getEndTime()) || timeEnd.equals(x.getEndTime()))
                .findFirst().orElse(null);
    }

    public List<TimeSlotDTO> getAllSelfSlotsByDate(LocalDate date, Authentication auth) {
        User worker = getAuthUser(auth);
        return getAllTimeSlotsByDateAndWorker(date, worker);
    }

    public List<TimeSlotDTO> getAllSelfSlotsByDateBetween(LocalDate dateFrom, LocalDate dateTo, Authentication auth) {
        User worker = getAuthUser(auth);
        return getAllTimeSlotsByDateBetweenAndWorker(dateFrom, dateTo, worker);
    }
}
