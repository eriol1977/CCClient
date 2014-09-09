/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccclient;

import com.fb.cc.bean.CrudService;
import com.fb.cc.entity.CC;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Francesco
 */
public class ReadDB
{
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception
    {
        CrudService service = lookup();
        
        List<CC> found = service.findByNamedQuery(CC.FIND_ALL);
        found.stream().forEach(cc -> System.out.println(cc));
    }
    
    private static CrudService lookup() throws NamingException
    {
        final Context context = new InitialContext();
        return (CrudService) context.lookup("ejb:/CCModule/CrudServiceBean!com.fb.cc.bean.CrudService");
    }
}
