package com.example.astonspringrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class UserService {
    private static final String TOPIC = "user-events";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public UserService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, UserRepository userRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    public UserDto create(UserDto dto) {
        validate(dto);
        User user = new User(dto.getName(), dto.getEmail(), dto.getAge());
        UserDto saved = toDto(userRepository.save(user));
        publish(UserEvent.created(saved));
        return saved;
    }

    @Transactional(readOnly = true)
    public UserDto getById(int id) {
        return toDto(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public UserDto update(int id, UserDto dto) {
        validate(dto);
        User user = findOrThrow(id);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        UserDto saved = toDto(userRepository.save(user));
        publish(UserEvent.updated(saved));
        return saved;
    }

    public void delete(int id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        userRepository.deleteById(id);
        publish(UserEvent.deleted(id));
    }

    private User findOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    private void validate(UserDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя не может быть пустым");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не может быть пустым");
        }
        if (dto.getAge() < 0 || dto.getAge() > 150) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный возраст");
        }
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }
    private void publish(UserEvent event){
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC,json);
        }catch (Exception e){
            throw new IllegalArgumentException("не удалось отправить событие в kafka", e);
        }
    }
}