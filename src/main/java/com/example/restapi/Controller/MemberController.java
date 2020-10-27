package com.example.restapi.Controller;

import com.example.restapi.Exception.AuthenticationException;
import com.example.restapi.Exception.InvalidRequestException;
import com.example.restapi.Model.MemberModel;
import com.example.restapi.Repository.MemberRepository;
import com.example.restapi.Exception.ResourceNotFoundException;
import com.example.restapi.Util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MemberController {
    @Autowired
    private MemberRepository memberRepositoy;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Util util = new Util();

    @PostMapping("/register")
    public MemberModel createMember(@RequestBody MemberModel request)
        throws InvalidRequestException {
        if((request.getPhoneNumber() != null && request.getPhoneNumber().isEmpty() == false) &&
                (request.getEmail() != null && request.getEmail().isEmpty() == false)){
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            return memberRepositoy.save(request);
        }else {
            throw new InvalidRequestException("Invalid Request");
        }
    }

    @PostMapping("/login")
    public Map<String, Object> loginMember(@RequestBody MemberModel request)
            throws AuthenticationException{
        MemberModel getUser = memberRepositoy.findUserByPhoneOrEmail(request.getPhoneNumber(), request.getEmail());
        Map<String, Object> response = new LinkedHashMap<>();

        if(getUser == null){
            throw new AuthenticationException("Authentication Failed");
        }else{
            boolean passwordMatch = passwordEncoder.matches(request.getPassword(), getUser.getPassword());
            if(passwordMatch){
                response.put("status", "Login Successfully");
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                String session = util.aesEncrypt(getUser.getId() + "::" +  currentTime.toString());

                memberRepositoy.UpdateLastActiveMember(session, request.getPhoneNumber(), request.getEmail());
            }else{
                throw new AuthenticationException("Authentication Failed");
            }
        }

        return response;
    }

    @GetMapping("/member")
    public ResponseEntity<MemberModel> getMemberById(@RequestParam Long id, @RequestBody MemberModel memberModel)
            throws ResourceNotFoundException, AuthenticationException {
        boolean sessionIsValid = util.sessionValidation(memberRepositoy, memberModel.getSessionId(), id, "getUser");

        if(sessionIsValid) {
            MemberModel member = memberRepositoy.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found for this id : " + id));

            return ResponseEntity.ok().body(member);
        }else{
            throw new AuthenticationException("Authentication Failed");
        }
    }

    @PutMapping("/member")
    public ResponseEntity<MemberModel> updateMember(@RequestParam Long id, @RequestBody MemberModel request)
            throws ResourceNotFoundException, AuthenticationException{
        boolean sessionIsValid = util.sessionValidation(memberRepositoy, request.getSessionId(), id, "getUser");

        if(sessionIsValid){
            MemberModel member = memberRepositoy.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found for this id : " + id));

            member.setFirstName(request.getFirstName());
            member.setLastName(request.getLastName());
            member.setGender(request.getGender());
            member.setDateOfBirth(request.getDateOfBirth());

            final MemberModel updateMember = memberRepositoy.save(member);
            return ResponseEntity.ok(updateMember);
        }else {
            throw new AuthenticationException("Authentication Failed");
        }
    }
}
