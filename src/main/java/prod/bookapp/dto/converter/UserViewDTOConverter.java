package prod.bookapp.dto.converter;

import org.springframework.stereotype.Component;
import prod.bookapp.dto.UserViewDTO;
import prod.bookapp.entity.User;

@Component
public class UserViewDTOConverter {
    public UserViewDTO convertToUserViewDTO(User user) {
        UserViewDTO userViewDTO = new UserViewDTO();
        userViewDTO.setId(user.getId());
        userViewDTO.setFirstName(user.getFirstName());
        userViewDTO.setLastName(user.getLastName());
        userViewDTO.setEmail(user.getEmail());
        userViewDTO.setCompanyName(user.getCompanyName());
        userViewDTO.setPhoneNumber(user.getPhoneNumber());
        userViewDTO.setShortDescription(user.getShortDescription());
        userViewDTO.setDescription(user.getDescription());
        userViewDTO.setDefaultCurrency(user.getDefaultCurrency());
        return userViewDTO;
    }
}
