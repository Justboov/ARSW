/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {
    
    private static final int BLACK_LIST_ALARM_COUNT=5;
    private int totalOcurrenceCount;
    private LinkedList<Integer> blackListOcurrences = new LinkedList<>();
    private LinkedList<SearchThread> threadList = new LinkedList<>();
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.The search is not exhaustive: When the number of occurrences is equal to BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @param N
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N){
        // Variables para determinar número de hilos y cuantos listas negras analizará cada uno.
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int division = skds.getRegisteredServersCount() / N;
        int residuo = skds.getRegisteredServersCount() % N;
        int numInicial = 0;
        
        int ocurrencesCount=0;
        int checkedListsCount=0;
        
        // Crea los N hilos y cuantas listas negras analizará cada uno.
        for (int i=0; i<N; i++) {
            if (residuo != 0) {
                SearchThread hilo = new SearchThread(ipaddress, numInicial, numInicial+division+1);
                hilo.start();
                threadList.add(hilo);
                numInicial = numInicial + division + 1;
                residuo--;
            }else {
                SearchThread hilo = new SearchThread(ipaddress, numInicial, numInicial+division);
                hilo.start();
                threadList.add(hilo);
                numInicial = numInicial + division;
            }
        }
        
        // Espera a que los hilos se detengan y luego consulta los datos de cada uno.
        for(int i=0; i<N; i++){
            try {
                threadList.get(i).join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HostBlackListsValidator.class.getName()).log(Level.SEVERE, null, ex);
            }
            blackListOcurrences.addAll(threadList.get(i).getBlackListOcurrences());
            checkedListsCount += threadList.get(i).getCheckedListCount();
            ocurrencesCount += threadList.get(i).getOcurrenceCount();
        }
        
        // Valida si es segura o no según el número de ocurrencias.
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        } 

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        return blackListOcurrences;
    }
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
}
