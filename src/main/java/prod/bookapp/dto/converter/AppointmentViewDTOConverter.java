package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.AppointmentViewClientDTO;
import prod.bookapp.dto.AppointmentViewDTO;
import prod.bookapp.dto.AppointmentViewWorkerDTO;
import prod.bookapp.entity.Appointment;

@Component
public class AppointmentViewDTOConverter {
    private final UserViewDTOConverter userViewDTOConverter;
    private final ProposalViewDTOConverter proposalViewDTOConverter;
    private final VenueViewDTOConverter venueViewDTOConverter;


    public AppointmentViewDTOConverter(UserViewDTOConverter userViewDTOConverter, ProposalViewDTOConverter proposalViewDTOConverter, VenueViewDTOConverter venueViewDTOConverter) {
        this.userViewDTOConverter = userViewDTOConverter;
        this.proposalViewDTOConverter = proposalViewDTOConverter;
        this.venueViewDTOConverter = venueViewDTOConverter;
    }

    public AppointmentViewDTO convertToAppointmentViewDTO(Appointment appointment) {

        AppointmentViewDTO appointmentViewDTO = new AppointmentViewDTO();
        appointmentViewDTO.setId(appointment.getId());
        appointmentViewDTO.setProposal(proposalViewDTOConverter.convertToProposalBookingViewDTO(appointment.getProposal()));
        appointmentViewDTO.setClient(userViewDTOConverter.convertToUserViewDTO(appointment.getClient()));
        appointmentViewDTO.setStatus(appointment.getStatus());
        return appointmentViewDTO;
    }

    public AppointmentViewWorkerDTO convertToAppointmentViewWorkerDTO(Appointment appointment) {
        AppointmentViewWorkerDTO appointmentViewWorkerDTO = new AppointmentViewWorkerDTO();
        appointmentViewWorkerDTO.setId(appointment.getId());
        appointmentViewWorkerDTO.setProposal(proposalViewDTOConverter.convertToProposalBookingViewDTO(appointment.getProposal()));
        appointmentViewWorkerDTO.setVenue(venueViewDTOConverter.convertToViewDTO(appointment.getVenue()));
        appointmentViewWorkerDTO.setClient(userViewDTOConverter.convertToUserViewDTO(appointment.getClient()));
        appointmentViewWorkerDTO.setStatus(appointment.getStatus());
        appointmentViewWorkerDTO.setDate(appointment.getDate());
        appointmentViewWorkerDTO.setTimeStart(appointment.getTimeStart());
        appointmentViewWorkerDTO.setTimeEnd(appointment.getTimeEnd());
        appointmentViewWorkerDTO.setDurationMin(appointment.getDurationMin());
        return appointmentViewWorkerDTO;
    }

    public AppointmentViewClientDTO convertToAppointmentViewClientDTO(Appointment appointment) {
        AppointmentViewClientDTO appointmentViewClientDTO = new AppointmentViewClientDTO();
        appointmentViewClientDTO.setId(appointment.getId());
        appointmentViewClientDTO.setProposal(proposalViewDTOConverter.convertToProposalBookingViewDTO(appointment.getProposal()));
        appointmentViewClientDTO.setVenue(venueViewDTOConverter.convertToViewDTO(appointment.getVenue()));
        appointmentViewClientDTO.setWorker(userViewDTOConverter.convertToUserViewDTO(appointment.getWorker()));
        appointmentViewClientDTO.setStatus(appointment.getStatus());
        appointmentViewClientDTO.setDate(appointment.getDate());
        appointmentViewClientDTO.setTimeStart(appointment.getTimeStart());
        appointmentViewClientDTO.setTimeEnd(appointment.getTimeEnd());
        appointmentViewClientDTO.setDurationMin(appointment.getDurationMin());
        return appointmentViewClientDTO;
    }
}
