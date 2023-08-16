package ua.edu.nau.darkrymit.fss.user;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ua.edu.nau.darkrymit.fss.user.payload.UserMiniResponse;

@Service
@RequiredArgsConstructor
public class UserService {

  private final WebClient webClient;

  private final UserOAuthMapper userOAuthMapper;

  public UserMiniResponse getByIdMini(UUID id){
    UserOAuthDetails userOAuthDetails = webClient
        .get()
        .uri("/users/{id}",id.toString())
        .retrieve()
        .bodyToMono(UserOAuthDetails.class)
        .blockOptional()
        .orElseThrow();
    return userOAuthMapper.toMiniResponse(userOAuthDetails);
  }

}
