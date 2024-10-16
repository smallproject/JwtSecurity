package nl.novi.les17JWT.mappers;

import nl.novi.les17JWT.entities.User;
import nl.novi.les17JWT.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper{

    private final RoleMapper roleMapper;


    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;

    }

    public UserModel fromEntity(User entity) {
        if (entity == null) {
            return null;
        }
        UserModel model = new UserModel(entity.getId());
        model.setPassword(entity.getPassword());
        model.setUserName(entity.getUserName());
        model.areCredentialsExpired(entity.areCredentialsExpired());
        model.setEnabled(entity.isEnabled());
        model.setExpired(entity.isExpired());
        model.setLocked(entity.isLocked());
        model.setRoles(roleMapper.fromEntities(entity.getRoles()));
        return model;
    }

    public User toEntity(UserModel model) {
        if (model == null) {
            return null;
        }
        User entity = new User(model.getId());
        entity.setPassword(model.getPassword());
        entity.setUserName(model.getUserName());
        entity.setAreCredentialsExpired(model.areCredentialsExpired());
        entity.setEnabled(model.isEnabled());
        entity.setExpired(model.isExpired());
        entity.setLocked(model.isLocked());
        entity.setRoles(roleMapper.toEntities(model.getRoles()));
        return entity;
    }
}

