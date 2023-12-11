package localgaji.albbaim.user.userDTO;

import localgaji.albbaim.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateUserDTO {
    private final String userName;
    private final Boolean isAdmin;
    @Builder
    public CreateUserDTO(String userName, Boolean isAdmin) {
        this.userName = userName;
        this.isAdmin = isAdmin;
    }

    public User createNewUser() {
        return User.builder()
                .userName(this.userName)
                .isAdmin(this.isAdmin)
                .build();
    }
}
