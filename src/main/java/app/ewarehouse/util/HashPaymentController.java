package app.ewarehouse.util;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.ewarehouse.dto.PaymentDataStringDto;

@RestController
@RequestMapping("/payment")
public class HashPaymentController {
	private final PaymentSecureHashGenerate secureHashGen;

    
    public HashPaymentController(PaymentSecureHashGenerate secureHashGen) {
        this.secureHashGen = secureHashGen;
    }

    @PostMapping("/generate-hash")
    public String generateHash(@RequestBody PaymentDataStringDto dataStringDto) {
        return secureHashGen.generateSignature(dataStringDto);
    }
}
