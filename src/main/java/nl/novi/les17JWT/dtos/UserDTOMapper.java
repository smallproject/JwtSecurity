package nl.novi.les17JWT.dtos;


import nl.novi.les17JWT.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {



    public UserModel mapToModel(UserRequestDTO userDTO) {
        var result = new UserModel(-1L);
        result.setUserName(userDTO.getUserName());
        result.setPassword(userDTO.getPassword());
        return result;
    }

    public UserModel mapToModel(UserChangePassWordRequestDTO userDTO, Long id) {
        var result = new UserModel(id);
        result.setPassword(userDTO.getPassword());
        return result;
    }
}
