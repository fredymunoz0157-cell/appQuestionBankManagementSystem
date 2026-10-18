package co.edu.unicauca.microservices.pkgQuestionService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tbl_question")

public class clsControllerQuestionSrv {
    private final IQuestionRepository attIQuestionRepository;

    public clsControllerQuestionSrv(IQuestionRepository prmIQuestionRepository) {
        attIQuestionRepository = prmIQuestionRepository;
    }

    @GetMapping
    public List<clsQuestion> opListAll() {
        return attIQuestionRepository.findAll();
    }

    @PostMapping
    public clsQuestion opCreate(@RequestBody clsQuestion prmQuestion) {
        return attIQuestionRepository.save(prmQuestion);
    }

    @GetMapping("/{question_ouid}")
    public clsQuestion opGetForOUID(@PathVariable("question_ouid") String prmOUID) {
        return attIQuestionRepository.findById(prmOUID).orElse(null);
    }

    @PutMapping("/{question_ouid}")
    public clsQuestion opUpdate(@PathVariable("question_ouid") String prmOUID, @RequestBody clsQuestion prmQuestion) {
        prmQuestion.setOUID(prmOUID);
        return attIQuestionRepository.save(prmQuestion);
    }

    @DeleteMapping("/{question_ouid}")
    public void opDelete(@PathVariable("question_ouid") String prmOUID) {
        attIQuestionRepository.deleteById(prmOUID);
    }

    @GetMapping("/by-user/{prmUser}")
    public List<clsQuestion> opGetQuestionsUser(@PathVariable("prmUser") String prmUser) {
        return attIQuestionRepository.findByAttQuestionUser(prmUser);
    }

    @GetMapping("/search")
    public List<clsQuestion> opSearchByUser(@RequestParam("prmUser") String prmUser) {
        return attIQuestionRepository.findByAttQuestionUserContainingIgnoreCase(prmUser);
    }
}