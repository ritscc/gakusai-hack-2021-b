package dev.abelab.gifttree.service;

import mockit.Injectable;
import mockit.Tested;

import dev.abelab.gifttree.repository.UserRepository;
import dev.abelab.gifttree.util.AuthUtil;

public class AuthService_UT extends AbstractService_UT {

	@Injectable
	UserRepository userRepository;

	@Injectable
	AuthUtil authUtil;

	@Tested
	AuthService authService;

}
