package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserStorageTest {

	@Autowired
	private UserStorage userStorage;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setEmail("test@example.com");
		user.setLogin("testuser");
		user.setName("Test User");
		user.setBirthday(Date.valueOf("2000-01-01"));
	}

	@Test
	void addUserTest() {
		User addedUser = userStorage.add(user);
		assertThat(addedUser).isNotNull();
		assertThat(addedUser.getId()).isGreaterThan(0);
	}

	@Test
	void updateUserTest() {
		User addedUser = userStorage.add(user);
		addedUser.setName("Updated User");
		User updatedUser = userStorage.update(addedUser);

		assertThat(updatedUser).isNotNull();
		assertThat(updatedUser.getName()).isEqualTo("Updated User");
	}

	@Test
	void getUserTest() {
		User addedUser = userStorage.add(user);
		User retrievedUser = userStorage.get(addedUser.getId());

		assertThat(retrievedUser).isNotNull();
		assertThat(retrievedUser.getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	void getAllUsersTest() {
		userStorage.add(user);

		User anotherUser = new User();
		anotherUser.setEmail("test2@example.com");
		anotherUser.setLogin("testuser2");
		anotherUser.setName("Test User 2");
		anotherUser.setBirthday(Date.valueOf("1995-05-15"));

		userStorage.add(anotherUser);

		Collection<User> users = userStorage.getAll();
		assertThat(users).hasSize(2);
	}

	@Test
	void addAndDeleteFriendTest() {
		User user1 = userStorage.add(user);

		User friend = new User();
		friend.setEmail("friend@example.com");
		friend.setLogin("frienduser");
		friend.setName("Friend User");
		friend.setBirthday(Date.valueOf("1998-03-10"));
		User user2 = userStorage.add(friend);

		userStorage.addFriend(user1.getId(), user2.getId());

		Collection<User> friends = userStorage.getAllFriends(user1.getId());
		assertThat(friends).contains(user2);

		boolean isDeleted = userStorage.deleteFromFriends(user1.getId(), user2.getId());
		assertThat(isDeleted).isTrue();

		friends = userStorage.getAllFriends(user1.getId());
		assertThat(friends).isEmpty();
	}

	@Test
	void getCommonFriendsTest() {
		User user1 = userStorage.add(user);

		User friend1 = new User();
		friend1.setEmail("friend1@example.com");
		friend1.setLogin("frienduser1");
		friend1.setName("Friend User 1");
		friend1.setBirthday(Date.valueOf("1995-04-05"));
		User user2 = userStorage.add(friend1);

		User friend2 = new User();
		friend2.setEmail("friend2@example.com");
		friend2.setLogin("frienduser2");
		friend2.setName("Friend User 2");
		friend2.setBirthday(Date.valueOf("1997-08-20"));
		User user3 = userStorage.add(friend2);

		userStorage.addFriend(user1.getId(), user3.getId());
		userStorage.addFriend(user2.getId(), user3.getId());

		Collection<User> commonFriends = userStorage.getCommonFriends(user1.getId(), user2.getId());
		assertThat(commonFriends).containsExactly(user3);
	}
}
