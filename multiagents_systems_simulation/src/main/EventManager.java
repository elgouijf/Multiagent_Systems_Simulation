package main;
import java.util.ArrayList;
/**
 * Gestionnaire d'événements discrets.
 * Il maintient une liste d'événements ordonnés et exécute ceux dont la date correspond au temps courant.
 */
public class EventManager {
    private long currentDate;
    private ArrayList<Event> events;
    // Constructor
    public EventManager(){
        this.currentDate = 0;
        this.events = new ArrayList<>() ;

    } 
    /**
     * Ajoute un événement à la liste des événements à exécuter.
     * @param e l'événement à ajouter
     */
    public void addEvent(Event e){
        events.add(e);
    }
    /**
     * retourne true si plus aucun évènement n’est en attente d’exécution.
     */
    public boolean isFinished(){
        return events.isEmpty();
    }
    /**
     *  Cette méthode incrémente  la date courante puis exécute dans l’ordre tous les évènements.
        non encore exécutés jusqu’à cette date
     */
    public void next(){
        currentDate++;
        ArrayList<Event> to_remove = new ArrayList<>();
        for(Event e : events){
            if(e.getDate() <= currentDate){
                e.execute();
                to_remove.add(e);
            }
        }
        events.removeAll(to_remove);
    }
    /**
     *  Cette méthode réinitialise les attribus du gestionnaire.
     */
    public void restart(){
        currentDate=0;
        events.clear();

    }
    
}