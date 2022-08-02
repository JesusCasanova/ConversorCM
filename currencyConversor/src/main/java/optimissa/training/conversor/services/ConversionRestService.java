package optimissa.training.conversor.services;

import optimissa.training.conversor.model.Amount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static optimissa.training.conversor.ConversorApplication.listaEquivalenciasUSD;

@RestController
public class ConversionRestService {

    /**
     * RequestBody para que el contenido que vamos a recibir a través
     * la petición get lo podamos ver
     *
     * le decimos por parametro que currencyOut es una variable, que luego será
     * una cadena y que la buscaremos en la url por ese mismo nombre y la convertimos a parametro de entrada
     * de la función
     *
     * @param amountIn Objeto de tipo amount con el importe y moneda de entrada solicitada por el usuario
     * @return Amount objeto de tipo amount con el importe y moneda de salida solicitada por el usuario
     */
    @GetMapping(value= "/change/{currencyOut}")
    private Amount doConversion(@PathVariable(name = "currencyOut") String currencyOut, @RequestBody Amount amountIn) {
        Amount amountOut = new Amount();
        amountOut.setCurrency(currencyOut);

        float cambioInToUSD= 0f;
        float cambioUSDToOut= 0f;
        //para recorrer todas las equivalencias y en amount guardamos cada elemento de la lista
        // y cambia por cada iteración
        for (Amount amount: listaEquivalenciasUSD){
            System.out.println("=>"+amount.getCurrency());
            System.out.println("==>"+amount.getValue());
            System.out.println("===>"+amountIn.getCurrency());
            System.out.println("====>"+currencyOut);
            if(amount.getCurrency().equals(amountIn.getCurrency())){
                //cuanto vale en DOLARES la moneda de entrada
                cambioInToUSD= amount.getValue();
            }
            if(amount.getCurrency().equals(currencyOut)){
                //cuanto vale en DOLARES la moneda de salida
                cambioUSDToOut= amount.getValue();
            }
        }
        amountOut.setValue(amountIn.getValue()*cambioInToUSD*cambioUSDToOut);

        return amountOut;
    }
}
