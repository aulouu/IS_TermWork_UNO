package com.is.uno.service;

import com.is.uno.dao.AchievementRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.api.AchievementDTO;
import com.is.uno.model.Achievement;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<AchievementDTO> getPlayerAchievements(String username) {
        User user = userService.findByUsername(username);
        return user.getAchievements().stream()
                .map(this::toAchievementDTO)
                .collect(Collectors.toList());
    }

    public void addAchievementToUser(String username, Achievement achievement) {
        User user = userService.findByUsername(username);
        Achievement finalAchievement = achievement;
        Optional<Achievement> existingAchievement = user.getAchievements().stream()
                .filter(a -> a.getName().equals(finalAchievement.getName()))
                .findFirst();

        if (existingAchievement.isEmpty()) {
            achievement = achievementRepository.save(achievement); // Сохраняем достижение перед добавлением в коллекцию
            user.getAchievements().add(achievement);
            userRepository.save(user);
        }
    }

    public Optional<AchievementDTO> addViewStatisticsAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement viewStatisticsAchievement = Achievement.builder()
                .name("Просмотр статистики")
                .description("Просмотрел статистику")
                .build();
        addAchievementToUser(username, viewStatisticsAchievement);
        return Optional.of(toAchievementDTO(viewStatisticsAchievement));
    }

    public Optional<AchievementDTO> addRegistrationAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement registrationAchievement = Achievement.builder()
                .name("Регистрация")
                .description("Зарегистрировался в игре")
                .build();
        addAchievementToUser(username, registrationAchievement);
        return Optional.of(toAchievementDTO(registrationAchievement));
    }

    public Optional<AchievementDTO> addFirstRoomCreationAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement firstRoomCreationAchievement = Achievement.builder()
                .name("Создание первой комнаты")
                .description("Создал первую игровую комнату")
                .build();
        addAchievementToUser(username, firstRoomCreationAchievement);
        return Optional.of(toAchievementDTO(firstRoomCreationAchievement));
    }

    public Optional<AchievementDTO> addFirstWinAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement firstWinAchievement = Achievement.builder()
                .name("Первая победа")
                .description("Одержал первую победу")
                .build();
        addAchievementToUser(username, firstWinAchievement);
        return Optional.of(toAchievementDTO(firstWinAchievement));
    }

    public Optional<AchievementDTO> addFiveWinAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement firstWinAchievement = Achievement.builder()
                .name("Пять побед")
                .description("Одержал пять побед")
                .build();
        addAchievementToUser(username, firstWinAchievement);
        return Optional.of(toAchievementDTO(firstWinAchievement));
    }

    public Optional<AchievementDTO> addTenWinAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement firstWinAchievement = Achievement.builder()
                .name("Десять побед")
                .description("Одержал десять побед")
                .build();
        addAchievementToUser(username, firstWinAchievement);
        return Optional.of(toAchievementDTO(firstWinAchievement));
    }

    public Optional<AchievementDTO> addFirstPlayAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement firstWinAchievement = Achievement.builder()
                .name("Первая игра")
                .description("Сыграл первую игру")
                .build();
        addAchievementToUser(username, firstWinAchievement);
        return Optional.of(toAchievementDTO(firstWinAchievement));
    }

    public Optional<AchievementDTO> addFivePlayAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement firstWinAchievement = Achievement.builder()
                .name("Пятая игра")
                .description("Сыграл пять игр")
                .build();
        addAchievementToUser(username, firstWinAchievement);
        return Optional.of(toAchievementDTO(firstWinAchievement));
    }

    public Optional<AchievementDTO> addTenPlayAchievement(String username) {
        User user = userService.findByUsername(username);
        Achievement firstWinAchievement = Achievement.builder()
                .name("Десятая игра")
                .description("Сыграл десять игр")
                .build();
        addAchievementToUser(username, firstWinAchievement);
        return Optional.of(toAchievementDTO(firstWinAchievement));
    }

    private AchievementDTO toAchievementDTO(Achievement achievement) {
        return AchievementDTO.builder()
                .name(achievement.getName())
                .description(achievement.getDescription())
                .build();
    }
}
