package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.AppointmentViewDTO;
import prod.bookapp.dto.AppointmentViewWorkerDTO;
import prod.bookapp.entity.Appointment;

@Component
public class AppointmentViewDTOConverter {
    private final UserViewDTOConverter userViewDTOConverter;
    private final ProposalViewDTOConverter proposalViewDTOConverter;

    public AppointmentViewDTOConverter(UserViewDTOConverter userViewDTOConverter, ProposalViewDTOConverter proposalViewDTOConverter) {
        this.userViewDTOConverter = userViewDTOConverter;
        this.proposalViewDTOConverter = proposalViewDTOConverter;
    }

    public AppointmentViewDTO convertToAppointmentViewDTO(Appointment appointment) {

        AppointmentViewDTO appointmentViewDTO = new AppointmentViewDTO();
        appointmentViewDTO.setId(appointment.getId());
        appointmentViewDTO.setProposal(proposalViewDTOConverter.convertToProposalViewDTO(appointment.getProposal()));
        appointmentViewDTO.setClient(userViewDTOConverter.convertToUserViewDTO(appointment.getClient()));
        appointmentViewDTO.setStatus(appointment.getStatus());
        return appointmentViewDTO;
    }

    public AppointmentViewWorkerDTO convertToAppointmentViewWorkerDTO(Appointment appointment) {
        AppointmentViewWorkerDTO appointmentViewWorkerDTO = new AppointmentViewWorkerDTO();
        appointmentViewWorkerDTO.setId(appointment.getId());
        appointmentViewWorkerDTO.setProposal(proposalViewDTOConverter.convertToProposalViewDTO(appointment.getProposal()));
        appointmentViewWorkerDTO.setClient(userViewDTOConverter.convertToUserViewDTO(appointment.getClient()));
        appointmentViewWorkerDTO.setStatus(appointment.getStatus());
        appointmentViewWorkerDTO.setDate(appointment.getDate());
        appointmentViewWorkerDTO.setTimeStart(appointment.getTimeStart());
        appointmentViewWorkerDTO.setTimeEnd(appointment.getTimeEnd());
        appointmentViewWorkerDTO.setDurationMin(appointment.getDurationMin());
        return appointmentViewWorkerDTO;
    }

    public AppointmentViewWorkerDTO convertToAppointmentViewClientDTO(Appointment appointment) {
        AppointmentViewWorkerDTO appointmentViewWorkerDTO = new AppointmentViewWorkerDTO();
        appointmentViewWorkerDTO.setId(appointment.getId());
        appointmentViewWorkerDTO.setProposal(proposalViewDTOConverter.convertToProposalViewDTO(appointment.getProposal()));
        appointmentViewWorkerDTO.setClient(userViewDTOConverter.convertToUserViewDTO(appointment.getWorker()));
        appointmentViewWorkerDTO.setStatus(appointment.getStatus());
        appointmentViewWorkerDTO.setDate(appointment.getDate());
        appointmentViewWorkerDTO.setTimeStart(appointment.getTimeStart());
        appointmentViewWorkerDTO.setTimeEnd(appointment.getTimeEnd());
        appointmentViewWorkerDTO.setDurationMin(appointment.getDurationMin());
        return appointmentViewWorkerDTO;
    }
}
