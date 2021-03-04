package lt2021.projektas.userRegister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt2021.projektas.child.ChildService;
import lt2021.projektas.child.ChildStatusObject;
import lt2021.projektas.kindergarten.queue.QueueService;
import lt2021.projektas.parentdetails.ParentDetailsDao;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ParentDetailsDao detailsDao;
	
	@Autowired
	private ChildService childService;
	
	@Autowired
	private QueueService queueService;

	@Transactional(readOnly = true)
	public List<ServiceLayerUser> getUsers() {
		var users = userDao.findAll();
		return users.stream()
				.map(userFromService -> new ServiceLayerUser(userFromService.getId(), userFromService.getFirstname(),
						userFromService.getLastname(), userFromService.getEmail(), userFromService.getPassword(),
						userFromService.getRole(), userFromService.isMarkedForDeletion()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ServiceLayerUser getSingleUser(long id) {
		var userFromService = userDao.findById(id).orElse(null);
		return new ServiceLayerUser(userFromService.getId(), userFromService.getFirstname(),
				userFromService.getLastname(), userFromService.getEmail(), userFromService.getPassword(),
				userFromService.getRole(), userFromService.isMarkedForDeletion());
	}

	@Transactional
	public void createUser(CreateUserCommand newUser) {
		User userToSave = new User(newUser.getFirstname(), newUser.getLastname(), newUser.getEmail().toLowerCase(),
				newUser.getRole());
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		userToSave.setPassword(encoder.encode(newUser.getFirstname()));
		userDao.save(userToSave);
	}

	@Transactional
	public void updateUser(ServiceLayerUser user) {

		var updatedUser = userDao.findById(user.getId()).orElse(null);
		updatedUser.setFirstname(user.getFirstname());
		updatedUser.setLastname(user.getLastname());
		updatedUser.setEmail(user.getEmail().toLowerCase());
		updatedUser.setRole(user.getRole());
		updatedUser.setMarkedForDeletion(user.isMarkedForDeletion());
		userDao.save(updatedUser);
	}

	@Transactional
	public void deleteUser(Long id) {
		var user = userDao.findById(id).orElse(null);
		if (user != null) {
			var parentDetails = user.getParentDetails();
			if (parentDetails != null) {
				var children = parentDetails.getChildren().stream().collect(Collectors.toList());
				if (children.size() > 0) {
					children.forEach(child -> {
						childService.deleteChild(id, child.getId());
					});
					children.clear();
					parentDetails.setChildren(null);
					detailsDao.delete(parentDetails);
					userDao.delete(user);
				} else {
					detailsDao.delete(parentDetails);
					userDao.delete(user);
				}
			} else {
				userDao.delete(user);
			}
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username + " not found.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				AuthorityUtils.createAuthorityList(new String[] { "ROLE_" + user.getRole().toString() }));
	}

	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	@Transactional
	public ResponseEntity<String> changePassword(User user, String oldPassword, String newPassword) {
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		if (oldPassword.equals(newPassword)) {
			return new ResponseEntity<String>("Senas ir naujas slaptažodis negali sutapti", HttpStatus.BAD_REQUEST);
		}
		if (encoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(encoder.encode(newPassword));
			userDao.save(user);
			return new ResponseEntity<String>("Slaptažodis pakeistas", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Neteisingai įvestas senas slaptažodis", HttpStatus.BAD_REQUEST);
	}
	
	@Transactional
	public UserStatusObject returnLoggedUserStatus(String email) {
		var user = findByEmail(email);
		UserStatusObject status = new UserStatusObject();
		List<ChildStatusObject> childrenStatus = new ArrayList<>();
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		status.setPasswordChanged(encoder.matches(user.getFirstname(), user.getPassword()) ? false : true);
		status.setDetailsFilled(user.getParentDetails() == null ? false : true);
		if (user.getParentDetails() != null) {
			status.setChildRegistered(user.getParentDetails().getChildren().size() == 0 ? false : true);
			if (user.getParentDetails().getChildren().size() > 0) {
				user.getParentDetails().getChildren().forEach(child -> {
					var childStatus = new ChildStatusObject();
					childStatus.setFirstname(child.getFirstname());
					childStatus.setLastname(child.getLastname());
					childStatus.setApplicationFilled(child.getRegistrationForm() == null ? false : true);
					Date today = new Date();
					var timeDiff = today.getTime() - child.getBirthdate().getTime();
					var timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
					var yearDiff = timeDiffDays / 365;
					if (child.getRegistrationForm() != null) {
						childStatus.setApplicationAccepted(child.getRegistrationForm().getAdmission() == null ? false : true);
						if (child.getRegistrationForm().getAdmission() == null) {
							if (yearDiff < 2) {
								childStatus.setNotAcceptedReason("Vaikas per jaunas darželiams");
								childrenStatus.add(childStatus);
							} else {
								childStatus.setNotAcceptedReason("Vaikas per senas darželiams");
								childrenStatus.add(childStatus);
							}
						} else {
							childStatus.setPlaceInFirstQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getFirstPriority(), child.getRegistrationForm()));
							childStatus.setPlaceInSecondQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getSecondPriority(), child.getRegistrationForm()));
							childStatus.setPlaceInThirdQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getThirdPriority(), child.getRegistrationForm()));
							childStatus.setPlaceInFourthQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getFourthPriority(), child.getRegistrationForm()));
							childStatus.setPlaceInFifthQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getFifthPriority(), child.getRegistrationForm()));
							childStatus.setAcceptedKindergarten(child.getRegistrationForm().getAcceptedKindergarten());
							childrenStatus.add(childStatus);
						}
					} else {
						childStatus.setApplicationAccepted(false);
						childrenStatus.add(childStatus);
					}
				});
			}
		}
		childrenStatus.sort((c1, c2) -> {
			if (c1.getLastname().equals(c2.getLastname())) {
				return c1.getFirstname().compareTo(c2.getFirstname());
			} else {
				return c1.getLastname().compareTo(c2.getLastname());
			}
		});
		status.setChildren(childrenStatus);
		return status;
	}

}
