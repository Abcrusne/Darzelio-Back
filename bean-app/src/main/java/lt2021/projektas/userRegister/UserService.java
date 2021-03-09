package lt2021.projektas.userRegister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import lt2021.projektas.kindergarten.Kindergarten;
import lt2021.projektas.kindergarten.KindergartenDao;
import lt2021.projektas.kindergarten.KindergartenStatisticsObject;
import lt2021.projektas.kindergarten.admission.AdmissionService;
import lt2021.projektas.kindergarten.queue.QueueService;
import lt2021.projektas.logging.Log;
import lt2021.projektas.logging.LogDao;
import lt2021.projektas.parentdetails.ParentDetailsDao;
import lt2021.projektas.passwordreset.PasswordResetDTO;
import lt2021.projektas.passwordreset.PasswordResetToken;
import lt2021.projektas.passwordreset.ResetTokenDao;

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
	
	@Autowired
	private KindergartenDao kindergartenDao;
	
	@Autowired
	private AdmissionService admissionService;
	
	@Autowired
	private ResetTokenDao tokenDao;
	
	@Autowired
	private LogDao logDao;
	
	private JavaMailSender emailSender;
	
	@Autowired
	public UserService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

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
	public void createUser(CreateUserCommand newUser, User admin) {
		User userToSave = new User(newUser.getFirstname(), newUser.getLastname(), newUser.getEmail().toLowerCase(),
				newUser.getRole());
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		userToSave.setPassword(encoder.encode(newUser.getFirstname()));
		userDao.save(userToSave);
		logDao.save(new Log(new Date(), admin.getEmail(), admin.getRole().toString(), ("Sukurtas naujas vartotojas su el. paštu: " + newUser.getEmail())));
	}

	@Transactional
	public void updateUser(ServiceLayerUser user, User loggedUser) {

		var updatedUser = userDao.findById(user.getId()).orElse(null);
		updatedUser.setFirstname(user.getFirstname());
		updatedUser.setLastname(user.getLastname());
		updatedUser.setEmail(user.getEmail().toLowerCase());
		updatedUser.setRole(user.getRole());
		updatedUser.setMarkedForDeletion(user.isMarkedForDeletion());
		userDao.save(updatedUser);
		logDao.save(new Log(new Date(), loggedUser.getEmail(), loggedUser.getRole().toString(), ("Pakeisti vartotojo " + user.getEmail() + " duomenys")));
	}

	@Transactional
	public void deleteUser(Long id, User loggedUser) {
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
			logDao.save(new Log(new Date(), loggedUser.getEmail(), loggedUser.getRole().toString(), ("Ištrintas vartotojas su el. paštu: " + user.getEmail())));
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
			logDao.save(new Log(new Date(), user.getEmail(), user.getRole().toString(), ("Bandytas pakeisti slaptažodis. Nepakeistas (senas ir naujas sutapo)")));
			return new ResponseEntity<String>("Senas ir naujas slaptažodis negali sutapti", HttpStatus.BAD_REQUEST);
		}
		if (encoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(encoder.encode(newPassword));
			userDao.save(user);
			logDao.save(new Log(new Date(), user.getEmail(), user.getRole().toString(), ("Pakeistas vartotojo slaptažodis")));
			return new ResponseEntity<String>("Slaptažodis pakeistas", HttpStatus.OK);
		}
		logDao.save(new Log(new Date(), user.getEmail(), user.getRole().toString(), ("Bandytas pakeisti slaptažodis. Nepakeistas (neteisingas senas slaptažodis)")));
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
		status.setAdmissionActive(admissionService.areAdmissionsActive());
		if (user.getParentDetails() != null) {
			status.setChildRegistered(user.getParentDetails().getChildren().size() == 0 ? false : true);
			if (user.getParentDetails().getChildren().size() > 0) {
				user.getParentDetails().getChildren().forEach(child -> {
					var childStatus = new ChildStatusObject();
					childStatus.setChildId(child.getId());
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
							childStatus.setFirstPriority(child.getRegistrationForm().getFirstPriority());
							childStatus.setPlaceInFirstQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getFirstPriority(), child.getRegistrationForm()));
							childStatus.setSecondPriority(child.getRegistrationForm().getSecondPriority());
							childStatus.setPlaceInSecondQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getSecondPriority(), child.getRegistrationForm()));
							childStatus.setThirdPriority(child.getRegistrationForm().getThirdPriority());
							childStatus.setPlaceInThirdQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getThirdPriority(), child.getRegistrationForm()));
							childStatus.setFourthPriority(child.getRegistrationForm().getFourthPriority());
							childStatus.setPlaceInFourthQueue(queueService.getChildPositionInKindergartenQueue(child.getRegistrationForm().getFourthPriority(), child.getRegistrationForm()));
							childStatus.setFifthPriority(child.getRegistrationForm().getFifthPriority());
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
	
	@Transactional
	public List<KindergartenStatisticsObject> getStatistics() {
		var kindergartens = kindergartenDao.findAll();
		List<KindergartenStatisticsObject> kindergartenStatistics = new ArrayList<>();
		for (Kindergarten kg : kindergartens) {
			var kgStat = new KindergartenStatisticsObject();
			kgStat.setKindergartenName(kg.getName());
			kg.getQueues().stream()
				.forEach(queue -> {
					var count = queue.getRegistrations().stream()
						.filter(reg -> reg.getFirstPriority().equals(queue.getKindergarten().getName()))
						.count();
					kgStat.setFirstPriorityRegistrations((kgStat.getFirstPriorityRegistrations() + (int) count));
				});
			kindergartenStatistics.add(kgStat);
		}
		return kindergartenStatistics;
	}
	
	@Transactional
	public ResponseEntity<String> resetUserPassword(String email) {
		var user = findByEmail(email);
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		if (user != null) {
			if (user.getToken() == null) {
				var token = new PasswordResetToken(user);
				user.setToken(token);
				user.setPassword(encoder.encode((new Date().toString() + user.getFirstname() + user.getLastname() )));
				userDao.save(user);
				token = tokenDao.save(token);
				final var finalToken = token;
				Thread newThread = new Thread(() -> {
					SimpleMailMessage message = new SimpleMailMessage();
					message.setFrom("bean.vaidar.mailinformer@gmail.com");
					message.setTo(user.getEmail());
					message.setSubject("Slaptažodžio keitimas");
					message.setText("Sveiki, " + user.getFirstname() + " " + user.getLastname() + ", \n"
							+ "Gautas prašymas pakeisti jūsų pamirštą slaptažodį. Nuoroda slaptažodžio keitimui: \n"
							+ "http://localhost:8081/bean-app/keistislaptazodis?token=" + finalToken.getToken());
					emailSender.send(message);
				});
				newThread.start();
				return new ResponseEntity<String>("Nuoroda slaptažodžio keitimui išsiųsta į nurodytą el. paštą", HttpStatus.OK);
			} else {
				var tokenToDelete = user.getToken();
				user.setToken(null);
				tokenDao.delete(tokenToDelete);
				var token = new PasswordResetToken(user);
				user.setToken(token);
				user.setPassword(encoder.encode((new Date().toString() + user.getFirstname() + user.getLastname() )));
				userDao.save(user);
				token = tokenDao.save(token);
				final var finalToken = token;
				Thread newThread = new Thread(() -> {
					SimpleMailMessage message = new SimpleMailMessage();
					message.setFrom("bean.vaidar.mailinformer@gmail.com");
					message.setTo(user.getEmail());
					message.setSubject("Slaptažodžio keitimas");
					message.setText("Sveiki, " + user.getFirstname() + " " + user.getLastname() + ", \n"
							+ "Gautas prašymas pakeisti jūsų pamirštą slaptažodį. Nuoroda slaptažodžio keitimui: \n"
							+ "http://localhost:8081/bean-app/keistislaptazodis?token=" + finalToken.getToken());
					emailSender.send(message);
				});
				newThread.start();
				return new ResponseEntity<String>("Nuoroda slaptažodžio keitimui išsiųsta į nurodytą el. paštą", HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>("Įvestas pašto adresas nerastas sistemoje", HttpStatus.BAD_REQUEST);
	}
	
	@Transactional
	public ResponseEntity<String> changeUserPasswordAfterReset(PasswordResetDTO resetObject) {
		@SuppressWarnings("deprecation")
		PasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-256");
		if (resetObject.getToken() != null && resetObject.getToken().length() > 0) {
			var token = tokenDao.findById(resetObject.getToken()).orElse(null);
			if (token != null) {
				if (new Date().after(token.getExpiryDate())) {
					tokenDao.delete(token);
					return new ResponseEntity<String>("Baigėsi slaptažodžio žymės (token) galiojimo laikas. Sukurkite naują prašymą atstatyti slaptažodį", HttpStatus.BAD_REQUEST);
				} else {
					if (resetObject.getNewPassword().equals(resetObject.getConfirmNewPassword())) {
						token.getUser().setPassword(encoder.encode(resetObject.getNewPassword()));
						userDao.save(token.getUser());
						tokenDao.delete(token);
						return new ResponseEntity<String>("Slaptažodis pakeistas sėkmingai", HttpStatus.OK);
					}
					return new ResponseEntity<String>("Įvesti slaptažodžiai nesutapo. Patikrinkite ar teisingai patvirtinote naują slaptažodį", HttpStatus.BAD_REQUEST);
				}
			}
			return new ResponseEntity<String>("Atsiųsta bloga slaptažodžio žymė (token)", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Atsiųsta bloga slaptažodžio žymė (token)", HttpStatus.BAD_REQUEST);
	}

}
