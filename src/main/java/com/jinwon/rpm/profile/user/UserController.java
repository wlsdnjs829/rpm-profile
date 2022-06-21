package com.jinwon.rpm.profile.user;

import com.jinwon.rpm.profile.base.BaseController;
import com.jinwon.rpm.profile.enums.ErrorMessage;
import com.jinwon.rpm.profile.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로필 회원 컨트롤러")
public class UserController extends BaseController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(description = "프로필 조회")
    public Mono<UserDto> getProfile(@NotNull Authentication authentication) {
        final Optional<User> userOp = getUserOp(authentication);
        final Supplier<CustomException> exceptionSupplier = () -> new CustomException(ErrorMessage.NOT_EXIST_USER);

        return userOp.map(User::getId)
                .map(userRepository::findById)
                .orElseThrow(exceptionSupplier)
                .map(UserDto::of)
                .map(Mono::just)
                .orElseThrow(exceptionSupplier);
    }

    @PostMapping
    @Operation(description = "프로필 생성")
    public Mono<User> createProfile(@Valid @RequestBody UserDto userDto) {
        return Mono.just(userRepository.save(User.of(userDto)));
    }

    @PutMapping
    @Operation(description = "프로필 전체 수정")
    public Mono<User> putProfile(@NotNull Authentication authentication, @Valid @RequestBody UserDto userDto) {
        final User user = getUserOp(authentication).orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_USER));

        return Mono.just(userRepository.save(User.of(userDto)));
    }

    @PatchMapping
    @Operation(description = "프로필 부분 수정")
    public Mono<User> patchProfile(@NotNull Authentication authentication, @Valid @RequestBody UserDto userDto) {
        return Mono.just(userRepository.save(User.of(userDto)));
    }

}
