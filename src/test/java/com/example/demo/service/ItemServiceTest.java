package com.example.demo.service;

import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
class ItemServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    @DisplayName("정상적인 사용자로 아이템 정상 생성")
    void createItemBy_whenUserExists() {

        // Given
        String name = "테스트 아이템 이름";
        String description = "테스트 아이템 설명";

        User owner = new User("user", "ghkfud6545@naver.com", "kim", "1234");
        User manager = new User("user", "manager@example.com", "lee", "1234");

        Item item = new Item(name, description, owner, manager);


        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(userRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(itemRepository.save(any())).thenReturn(item);

        // When
        itemService.createItem(name, description, owner.getId(), manager.getId());


    }

    @Test
    void createItem_ownerNotFound_throwsException() {
        // Given
        User owner = new User("user", "ghkfud6545@naver.com", "kim", "1234");
        User manager = new User("user", "manager@example.com", "lee", "1234");

        when(userRepository.findById(owner.getId())).thenReturn(Optional.empty());

        String name = "Test Item";
        String description = "Test Description";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> itemService.createItem(name, description, owner.getId(), manager.getId())
        );

        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void createItem_managerNotFound_throwsException() {
        // Given
        User owner = new User("user", "ghkfud6545@naver.com", "kim", "1234");
        User manager = new User("user", "manager@example.com", "lee", "1234");


        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(userRepository.findById(manager.getId())).thenReturn(Optional.empty());

        String name = "Test Item";
        String description = "Test Description";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> itemService.createItem(name, description, owner.getId(), manager.getId())
        );

        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", exception.getMessage());
    }

}