package DoubleLinkedList;

import java.util.Stack;

/**
 * Undo/Redo Functionality: In applications that support undo/redo functionality, a doubly linked list can be used to
 * maintain a sequence of states. Each state change is stored as a node in the list, allowing easy navigation between
 * previous and next states, enabling undoing and redoing of actions.
 * null<>1
 *1<>2<>3<>4<>5
 *     1
 * */

interface Command<T> {
    void execute();
    void undo();
    void redo();
}

class InsertCommand<T> implements Command<T> {
    private final UndoRedoManager<T> manager;
    private final T newState;

    public InsertCommand(UndoRedoManager<T> manager, T newState) {
        this.manager = manager;
        this.newState = newState;
    }

    @Override
    public void execute() {
        manager.performAction(newState);
    }

    @Override
    public void undo() {
        manager.undo();
    }

    @Override
    public void redo() {
        execute();
    }
}

public class UndoRedoManager<T> {
    private Stack<Command<T>> undoStack = new Stack<>();
    private Stack<Command<T>> redoStack = new Stack<>();

    public void performAnotherAction(T newState) {
        Command<T> command = new InsertCommand<>(this, newState);
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void theUndo() {
        if (!undoStack.isEmpty()) {
            Command<T> command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("No state to undo");
        }
    }

    public void theRedo() {
        if (!redoStack.isEmpty()) {
            Command<T> command = redoStack.pop();
            command.redo();
            undoStack.push(command);
        } else {
            System.out.println("No state to redo");
        }
    }


    private class Node{
        private T state;
        private Node prev;
        private Node next;
        private Node (T state){
            this.state = state;
        }
    }
    private Node currentState;
    public T undo(){
        if (currentState == null) {
            System.out.println("No state to undo");
            return null;
        }

        //Get the previous state
        Node previousState = currentState.prev;
        if (previousState == null) {
            System.out.println("Reached the initial State");
            return null;
        } else {
            //update the current state to the previos state
            currentState = previousState;
        }
        return currentState.state;
    }

    //Implement Redo Operation


    public void performAction (T newState){
        //create a new node for the new task
        Node newNode = new Node(newState);

        //Set the links for the new node
        newNode.prev = currentState;
        newNode.next = null;

        //Update the next link for the current state
        if(currentState !=null){
            currentState.next = newNode;
        }
        //update the current to the new state
        currentState = newNode;

    }

    //state1 <> State 2 <> State 3 <> State 4 <> State 5
    //state1 <> State 2 <> State 3 <> State 4


    public static void main(String[] args) {
        UndoRedoManager<String> undoRedoManager = new UndoRedoManager<>();
        undoRedoManager.performAction("State 1");
        undoRedoManager.performAction("State 2");
        undoRedoManager.theUndo();
        undoRedoManager.theRedo();

        System.out.println("Current state: " + undoRedoManager.currentState.state);

        undoRedoManager.performAction("State 3");
        undoRedoManager.performAction("State 4");
        undoRedoManager.performAction("State 5");

        System.out.println("Current state: " + undoRedoManager.currentState.state);
        undoRedoManager.undo();
        System.out.println("Current state: " + undoRedoManager.currentState.state);
        undoRedoManager.undo();
        System.out.println("Current state: " + undoRedoManager.currentState.state);
        undoRedoManager.undo();
        System.out.println("Current state: " + undoRedoManager.currentState.state);





    }
}

