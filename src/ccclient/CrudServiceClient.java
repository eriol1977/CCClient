/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccclient;

import com.fb.cc.bean.CrudService;
import com.fb.cc.entity.CC;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Francesco
 */
public class CrudServiceClient
{
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception
    {
        CrudService service = lookup();
        
        final CC ccOriginal = new CC(1, 10);
        CC ccCreated = service.create(ccOriginal);
        System.out.println("Creato: " + ccCreated);
        
        CC ccFound = service.find(ccOriginal.getClass(), ccOriginal.getId());
        System.out.println("Trovato: " + ccFound);
        
        ccOriginal.setSaldo(33);
        service.update(ccOriginal);
        System.out.println("Aggiornato: " + ccOriginal);
        
        ccFound = service.find(ccOriginal.getClass(), ccOriginal.getId());
        System.out.println("Trovato: " + ccFound);
        
        service.delete(ccOriginal.getClass(), ccOriginal.getId());
        System.out.println("Cancellato: " + ccOriginal);
        
        service.create(new CC(1, 10));
        service.create(new CC(2, 20));
        service.create(new CC(3, 30));
        service.create(new CC(4, 40));
        service.create(new CC(5, 50));
        
        Integer count = service.countResultsOfNamedQuery(CC.FIND_ALL);
        System.out.println("Trovati " + count + " CCs in totale");
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("saldo", 25);
        
        count = service.countResultsOfNamedQuery(CC.FIND_WITH_SALDO_HIGHER_THAN, parameters);
        System.out.println("Trovati " + count + " CCs con saldo superiore a 25");
        List<CC> results = service.findByNamedQuery(CC.FIND_WITH_SALDO_HIGHER_THAN, parameters);
        results.stream().forEach(cc -> {
            System.out.println("Trovato: " + cc);
        });
        
        for(int i=1; i<6; i++)
        {
            service.delete(CC.class, i);
        }
    }
    
    private static CrudService lookup() throws NamingException
    {
        final Context context = new InitialContext();
        return (CrudService) context.lookup("ejb:/CCModule/CrudServiceBean!com.fb.cc.bean.CrudService");
    }
}
