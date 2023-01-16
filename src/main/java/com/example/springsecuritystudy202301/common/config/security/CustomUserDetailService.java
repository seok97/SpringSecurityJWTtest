package com.example.springsecuritystudy202301.common.config.security;

import com.example.springsecuritystudy202301.sample.dao.UsrDao;
import com.example.springsecuritystudy202301.sample.dto.res.UsrResDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private UsrDao usrDao;

    /**
     * 로그인 시 DB 에서 유저 정보와 권한 정보를 조회 하고
     * 해당 정보를 기반 으로 User 객체를 생성하고 리턴한다.
    */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("authenticationManagerBuilder.getObject().authenticate() 가 실행되고 loadUserByUsername 호출");
        return createUser(getMember(username));
    }

    public UsrResDto getMember(String usrId) {
        UsrResDto userDto = usrDao.getUsrById(usrId);
        List<String> roles = usrDao.getRoles(usrId);
        userDto.setRoleList(roles);
        log.info("사용자 정보 조회 : {}", userDto);
        return userDto;
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUser(UsrResDto member) {
        // Collections<? extends GrantedAuthority>
        List<SimpleGrantedAuthority> authList = member.getRoleList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        authList.forEach(o-> log.debug("authList -> {}",o.getAuthority()));

        return new User(
                member.getUsrId(),
                member.getPswd(),
                authList
        );
    }

}
