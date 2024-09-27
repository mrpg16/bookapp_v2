package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.AppointmentViewClientDTO;
import prod.bookapp.dto.AppointmentViewDTO;
import prod.bookapp.dto.AppointmentViewWorkerDTO;
import prod.bookapp.entity.Appointment;

import java.util.Objects;

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
        appointmentViewDTO.setVenue(venueViewDTOConverter.convertToViewDTO(appointment.getVenue()));
        appointmentViewDTO.setClient(userViewDTOConverter.convertToUserViewDTO(appointment.getClient()));
        appointmentViewDTO.setStatus(appointment.getStatus());
        var ppId = appointment.getPricePackId();
        var pp = appointment.getProposal().getPricePacks().stream().filter(p -> Objects.equals(p.getId(), ppId)).findFirst().orElse(null);
        appointmentViewDTO.setPricePack(pp);
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
        var ppId = appointment.getPricePackId();
        var pp = appointment.getProposal().getPricePacks().stream().filter(p -> Objects.equals(p.getId(), ppId)).findFirst().orElse(null);
        appointmentViewWorkerDTO.setPricePack(pp);
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
        var ppId = appointment.getPricePackId();
        var pp = appointment.getProposal().getPricePacks().stream().filter(p -> Objects.equals(p.getId(), ppId)).findFirst().orElse(null);
        appointmentViewClientDTO.setPricePack(pp);
        return appointmentViewClientDTO;
    }
}
