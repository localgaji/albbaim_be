package localgaji.albbaim.user.userDTO;

import localgaji.albbaim.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateUserDTO {
    private final String userName;
    private final Boolean isAdmin;
    private final LocalDateTime createdAt;
    @Builder
    public CreateUserDTO(String userName, Boolean isAdmin) {
        this.userName = userName;
        this.isAdmin = isAdmin;
        this.createdAt = LocalDateTime.now();
    }

    public User createNewUser() {
        return User.builder()
                .userName(this.userName)
                .createdAt(this.createdAt)
                .isAdmin(this.isAdmin)
                .build();
    }
}
