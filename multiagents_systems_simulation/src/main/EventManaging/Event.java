package main.EventManaging;
/**
 * Classe abstraite représentant un événement dans le gestionnaire.
 * Chaque événement possède une date  à laquelle il doit être exécuté.
 * Les sous-classes qui représenteront des évènements réels avec  leurs propres propriétés  devront redéfinir la méthode execute() de manière adéquate.
 */

public abstract class Event implements Comparable<Event>{
    protected long date;
    // Constructor
    public Event(long date){
        this.date = date;
    }
    public long getDate(){
        return this.date;
    }
    public abstract void execute();
    @Override
    public int compareTo(Event other) {
        // Les événements avec les dates les plus petites en premier
        return Long.compare(this.date, other.date);
    }
}
