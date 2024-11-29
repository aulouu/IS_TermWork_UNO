package com.is.uno.service;

import com.is.uno.dao.GameRoomRepository;
import com.is.uno.dao.PlayerRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.GameRoom.GameRoomDTO;
import com.is.uno.dto.GameRoom.JoinGameRoomDTO;
import com.is.uno.exception.GameRoomNotFoundException;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    public GameRoomDTO createGameRoom(GameRoomDTO gameRoomDTO) {
        User owner = userRepository.findById(gameRoomDTO.getOwner())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Username %s not found", gameRoomDTO.getOwner())
                ));

        GameRoom gameRoom = GameRoom.builder()
                .roomName(gameRoomDTO.getRoomName())
                .password(gameRoomDTO.getPassword())
                .maxPlayers(gameRoomDTO.getMaxPlayers())
                .owner(owner)
                .build();

        gameRoom = gameRoomRepository.save(gameRoom);
        return toGameRoomDTO(gameRoom);
    }

    public GameRoomDTO joinGameRoom(JoinGameRoomDTO joinGameRoomDTO) {
        User user = userRepository.findByUsername(joinGameRoomDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Username %s not found", joinGameRoomDTO.getUsername())
                ));
        GameRoom gameRoom = gameRoomRepository.findById(joinGameRoomDTO.getRoomId())
                .orElseThrow(() -> new GameRoomNotFoundException(
                        String.format("Game room %s not found", joinGameRoomDTO.getRoomId())
                ));

        Player player = Player.builder()
                .inGameName(joinGameRoomDTO.getInGameName() != null ? joinGameRoomDTO.getInGameName() : user.getUsername())
                .userId(user)
                .currentRoomId(gameRoom)
                .build();

        playerRepository.save(player);
        return toGameRoomDTO(gameRoom);
    }

    public List<GameRoomDTO> getAllGameRooms() {
        List<GameRoom> gameRooms = gameRoomRepository.findAll();
        return gameRooms.stream()
                .map(this::toGameRoomDTO)
                .collect(Collectors.toList());
    }

    private GameRoomDTO toGameRoomDTO(GameRoom gameRoom) {
        return GameRoomDTO.builder()
                .roomName(gameRoom.getRoomName())
                .password(gameRoom.getPassword())
                .maxPlayers(gameRoom.getMaxPlayers())
                .owner(gameRoom.getOwner().getUsername())
                .build();
    }
}
