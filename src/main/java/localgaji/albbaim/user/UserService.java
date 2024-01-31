package localgaji.albbaim.user;

import localgaji.albbaim.auth.authDTO.RequestAuth.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 유저 추가
    public User makeNewUser(SignUpRequest signUpRequest) {
        User newUser = signUpRequest.toEntity();
        userRepository.save(newUser);
        return newUser;
    }
}
