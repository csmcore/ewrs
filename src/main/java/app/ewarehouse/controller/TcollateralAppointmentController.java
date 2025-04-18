package app.ewarehouse.controller;
import app.ewarehouse.service.TcollateralAppointmentService;
import app.ewarehouse.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("admin/collateralAppointment")
public class TcollateralAppointmentController {

    @Autowired
    TcollateralAppointmentService service;
    @Autowired
    ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(TcollateralAppointmentController.class);

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody String appointment) throws JsonProcessingException {
        String response = service.save(appointment);
        return ResponseEntity.ok(CommonUtil.encodedJsonResponse(response, objectMapper));
    }


}
