package main.EventManaging;
import java.util.PriorityQueue;

/**
 * Gestionnaire d'événements discrets.
 * Il maintient une liste d'événements ordonnés et exécute ceux dont la date correspond au temps courant.
 */
public class EventManager {
    private long currentDate;
    private PriorityQueue<Event> events;;
    // Constructor
    public EventManager(){
        this.currentDate = 0;
        this.events = new PriorityQueue<Event>() ;

    } 
    /**
     * retourne la "date" actuelle.
     */
    public long getCurrentDate(){
        return this.currentDate;
    }
    /**
     * retourne la liste des événemnts.
     */
    public PriorityQueue<Event> getEvents(){
        return this.events;
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
        while (!events.isEmpty() && events.peek().getDate() <= currentDate){
                Event e = events.poll(); // Récupère et supprime l'événement le plus prioritaire
                e.execute();
            }
    }
    /**
     *  Cette méthode réinitialise les attribus du gestionnaire.
     */
    public void restart(){
        currentDate=0;
        events.clear();

    }
    
}