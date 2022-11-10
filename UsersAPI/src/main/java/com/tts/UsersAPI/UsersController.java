package com.tts.UsersAPI;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

	@Autowired
	private UsersRepository repository;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers(@RequestParam(value = "state", required = false) @Valid String state) {

		if (state != null) {
			List<User> usersByState = repository.findByState(state);
			return new ResponseEntity<>(usersByState, HttpStatus.OK);
		}
		List<User> allUsers = (List<User>) repository.findAll();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

//	Return `ResponseEntity<Optional<User>>`
//    - Return a 404 Not Found response code if the user with the given ID does not exist
//    - Return a 200 OK HTTP response code along with the user data otherwise
//    - Note: because of how optionals work, your method will need to include the following:
//        ```java
//        Optional<User> user = repository.findById(id);
//        if (!user.isPresent()) {
//            ...
//        ```

	@GetMapping("/users/{id}")
	public ResponseEntity<Optional<User>> getUserByID(@PathVariable(value = "id") Long id) {

		Optional<User> found = (Optional<User>) repository.findById(id);

		if (!found.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
		return new ResponseEntity<>(found, HttpStatus.OK);

	}

//	Return `ResponseEntity<Void>`
//    - Validate the request body and store validation errors in a BindingResult object
//    - Return a 400 Bad Request HTTP response code if there are any validation errors
//    - Return a 201 Created HTTP response code otherwise

	@PostMapping("/users")
	public ResponseEntity<Void> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		repository.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	};

//	Return `ResponseEntity<Void>`
//    - Validate the request body and store validation errors in a BindingResult object
//    - Return a 404 Not Found response code if the user with the given ID does not exist
//    - Return a 400 Bad Request HTTP response code if there are any validation errors
//    - Return a 200 OK HTTP response code otherwise

	@PutMapping("/users/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable(value = "id") Long id, @RequestBody @Valid User user,
			BindingResult bindingResult) {

		Optional<User> found = repository.findById(id);

		if (bindingResult.hasFieldErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (found == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		repository.save(user);

		return new ResponseEntity<>(HttpStatus.OK);

	}

//	Return `ResponseEntity<Void>`
//    - Return a 404 Not Found response code if the user with the given ID does not exist
//    - Return a 200 OK HTTP response code otherwise
//	  
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id) {

		Optional<User> user = repository.findById(id);

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}