package gov.samhsa.c2s.pep.web;

import gov.samhsa.c2s.common.log.Logger;
import gov.samhsa.c2s.common.log.LoggerFactory;
import gov.samhsa.c2s.pep.service.PolicyEnforcementPoint;
import gov.samhsa.c2s.pep.service.dto.AccessRequestDto;
import gov.samhsa.c2s.pep.service.dto.AccessResponseDto;
import gov.samhsa.c2s.pep.service.dto.SegmentedDocumentsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
public class PolicyEnforcementPointRestController {

    private final Logger logger = LoggerFactory.getLogger(this);

    @Autowired
    private PolicyEnforcementPoint policyEnforcementPoint;

    @RequestMapping(value = "/access", method = RequestMethod.POST)
    public AccessResponseDto access(@Valid @RequestBody AccessRequestDto accessRequest, Principal principal) {
        logPrincipal(principal);
        return policyEnforcementPoint.accessDocument(accessRequest);
    }

    @RequestMapping(value = "/documents", method = RequestMethod.GET)
    public SegmentedDocumentsResponseDto getDocuments(@RequestParam("mrn") String mrn, @RequestParam("purposeOfUse") String purposeOfUse, @RequestParam("domain") String domain, Principal principal) {
        logPrincipal(principal);
        return policyEnforcementPoint.getCCDDocuments(principal.getName(), mrn, purposeOfUse, domain);
    }

    private void logPrincipal(Principal principal) {
        logger.debug(() -> "Handling request with principal: " + Optional.ofNullable(principal).map(Principal::getName).orElse("Principal.getName() not found"));
    }
}
