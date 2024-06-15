package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.member.MemberLink;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.LinkRequest;
import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.MemberInfoRequest;
import com.artistry.artistry.Dto.Request.MemberUpdateRequest;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.MemberTeamsResponse;
import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
import com.artistry.artistry.Repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;

    public MemberService(MemberRepository memberRepository,ApplicationRepository applicationRepository){
        this.memberRepository = memberRepository;
        this.applicationRepository = applicationRepository;
    }

    public Member findEntityById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public MemberResponse findById(Long id){
        return MemberResponse.from(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public MemberTeamsResponse getParticipatedTeams(final MemberInfoRequest request){
        final Member member = memberRepository.findById(request.getId())
                .orElseThrow(MemberNotFoundException::new);

        final List<Application> applications = applicationRepository.findByStatusAndPortfolio_Member(ApplicationStatus.APPROVED, member);

        final List<Team> teams = applications.stream()
                .map(Application::getTeam)
                .toList();

        return MemberTeamsResponse.from(teams);

    }

    public MemberResponse create(final MemberCreateRequest request){
        Member member = request.toEntity();
        validateDuplicateMember(member);

        memberRepository.save(member);

        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse update(final Long memberId, final MemberUpdateRequest request){
        validateDuplicateNickname(request.getNickName());

        Member member = findEntityById(memberId);
        member.update(request.getNickName(),request.getIconUrl(),createLinks(request.getLinks()));

        return MemberResponse.from(findEntityById(memberId));
    }

    public List<MemberLink> createLinks(List<LinkRequest> requests){
        return requests.stream()
                .map(LinkRequest::toMemberLink)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Member member){
        memberRepository.delete(member);
    }

    public MemberResponse findByEmail(String email){
        return MemberResponse.from(memberRepository.findByEmail(email));
    }

    private void validateDuplicateMember(Member member){
        validateDuplicateNickname(member.getNickname());
        validateDuplicateEmail(member.getEmail());
    }

    private void validateDuplicateNickname(String nickName){
        if(isNicknameExists(nickName)){
            throw new ArtistryDuplicatedException(String.format("[%s]는 이미 존재하는 닉네임입니다.", nickName));
        }
    }

    public Boolean isNicknameExists(String nickName){
        return memberRepository.existsByNickname_value(nickName);
    }

    private void validateDuplicateEmail(String email){
        if(isEmailExists(email)){
            throw new ArtistryDuplicatedException(String.format("[%s]는 이미 존재하는 이메일입니다.", email));
        }
    }

    public boolean isEmailExists(String email){
        return memberRepository.existsByEmail(email);
    }

}
