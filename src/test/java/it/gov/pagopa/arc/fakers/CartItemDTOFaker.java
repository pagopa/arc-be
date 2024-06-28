package it.gov.pagopa.arc.fakers;


import it.gov.pagopa.arc.model.generated.CartItemDTO;
import it.gov.pagopa.arc.model.generated.UserDetailDTO;

public class CartItemDTOFaker {
    public static CartItemDTO mockInstance(UserDetailDTO payee, UserDetailDTO debtor){
        return mockInstanceBuilder(payee, debtor).build();
    }
    private static CartItemDTO.CartItemDTOBuilder mockInstanceBuilder(UserDetailDTO payee, UserDetailDTO debtor){
        return CartItemDTO.builder()
                .subject("pagamento")
                .amount(545230L)
                .payee(payee)
                .debtor(debtor)
                .refNumberValue("960000000094659945")
                .refNumberType("IUV");
    }
}
