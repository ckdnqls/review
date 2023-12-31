package com.example.review.user.service;

import com.example.review.common.CommonResponseDto;
import com.example.review.common.UserRoleEnum;
import com.example.review.user.dto.LoginRequestDto;
import com.example.review.user.dto.SignupRequestDto;
import com.example.review.user.entity.User;
import com.example.review.user.repository.UserRepository;
import com.example.review.user.util.JwtUtil;
import com.example.review.user.util.Token;
import com.example.review.user.util.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        String adminPW = requestDto.getAdminPW();
        String confirmPassword = passwordEncoder.encode(requestDto.getConfirmPassword());

        //회원 중복 확인
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }
        //email 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }
        if(!requestDto.getPassword().equals(requestDto.getConfirmPassword())){
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 같지 않습니다.");
        }
        if(username.contains(requestDto.getPassword())){
            throw  new IllegalArgumentException("닉네임에 비밀번호가 포함되어있습니다.");
        }
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (adminPW!=null && !adminPW.isEmpty()) { // 관리자 암호를 보내주지 않거나 빈칸인 경우에는 if문에 들어가지 않습니다
            if (!jwtUtil.validateAdminPW(adminPW)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, email,confirmPassword, role);
        userRepository.save(user);
    }

    public ResponseEntity<CommonResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 등록된 유저인지 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이미 로그인 되어 있는지 확인
        if (!tokenRepository.findByUser(user).isEmpty()) {
            Token token = tokenRepository.findByUser(user).orElseThrow(()-> new IllegalArgumentException("존재하는데 존재하지 않는 발생할리 없는 에러"));
            String beareredToken = jwtUtil.BEARER_PREFIX+token.getTokenValue();
            response.setHeader(JwtUtil.AUTHORIZATION_HEADER,beareredToken);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new CommonResponseDto("이미 로그인 되어 있습니다.",HttpStatus.BAD_REQUEST.value()));
        }

        // header에 Role이 담긴 JwtToken탑재
        String bearerToken = jwtUtil.createToken(requestDto.getUsername(),user.getRole());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, bearerToken);

        // 토큰을 table에 넣는다.
        String token = bearerToken.substring(7);
        Token tokenObject = new Token(token,user);
        tokenRepository.save(tokenObject);

        // 관리자/일반유저 로그인 메세지 출력
        if (user.getRole()== UserRoleEnum.ADMIN) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("관리자님 환영합니다.",HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto("로그인 성공",HttpStatus.OK.value()));
        }
    }

    public void logout(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
            String token = bearerToken.substring(7);
            Token tokenObject = tokenRepository.findByTokenValue(token).orElseThrow(() -> new IllegalArgumentException("해당 토큰이 존재하지 않습니다."));
            tokenObject.setUser(null);  // Detach 전에 User 객체를 null로 설정
            tokenRepository.delete(tokenObject);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
