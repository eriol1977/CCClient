/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccclient;

import com.fb.cc.bean.CCRemoteAccess;
import com.fb.cc.entity.CC;
import com.fb.cc.exception.CCException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Francesco
 */
public class CCClient
{

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception
    {
        CCRemoteAccess ccManager = lookup("CMT");
        System.out.println("Gestione CC con Container Managed Transactions (CMT)");
        playWithAccounts(ccManager);
        
        System.out.println("****************************************************");
        
        ccManager = lookup("BMT");
        System.out.println("Gestione CC con Bean Managed Transactions (BMT)");
        playWithAccounts(ccManager);
    }

    private static void playWithAccounts(CCRemoteAccess ccManager) throws Exception
    {
        CC cc1 = new CC(1, 10);
        CC cc2 = new CC(2, 20);

        ccManager.incluirContaCorrente(cc1);
        ccManager.incluirContaCorrente(cc2);

        cc1 = ccManager.selecionarContaCorrente(1);
        System.out.println("Saldo cc1: " + cc1.getSaldo());
        cc2 = ccManager.selecionarContaCorrente(2);
        System.out.println("Saldo cc2: " + cc2.getSaldo());
        
        System.out.println("Trasferimento di 5 da cc1 a cc2...");
        ccManager.efetuarTransferencia(cc1, cc2, 5);
        
        
        cc1 = ccManager.selecionarContaCorrente(1);
        
        System.out.println("Saldo cc1 dopo trasferimento: " + cc1.getSaldo());
        System.out.println("Saldo cc1: " + cc1.getSaldo());
        cc2 = ccManager.selecionarContaCorrente(2);
        System.out.println("Saldo cc2: " + cc2.getSaldo());

        CC falseCC = new CC(null, 0);

        try {
            System.out.println("Trasferimento di 5 da cc1 a un cc inesistente...");
            ccManager.efetuarTransferencia(cc1, falseCC, 5);
        } catch (CCException e) {
            System.out.println("Tipo di eccezione: " + e.getMethod());
            cc1 = ccManager.selecionarContaCorrente(1);
            System.out.println("Saldo cc1: " + cc1.getSaldo());
        }
    }
    
    private static CCRemoteAccess lookup(final String kind) throws NamingException
    {
        final Context context = new InitialContext();
        final String beanName = kind.equals("CMT") ? "CCRemoteAccessWithCMT" : "CCRemoteAccessWithBMT";
        return (CCRemoteAccess) context.lookup("ejb:/CCModule/" + beanName + "!com.fb.cc.bean.CCRemoteAccess");
    }
}
