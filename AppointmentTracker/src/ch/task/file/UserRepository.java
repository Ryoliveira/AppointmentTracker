package ch.task.file;

import ch.task.user.UserProfile;

public interface UserRepository {

	void save(UserProfile profile);
	UserProfile load(String profileName);
}
