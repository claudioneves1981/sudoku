package br.com.dio.model;

import br.com.dio.enuns.GameStatusEnum;

import java.util.Collection;
import java.util.List;

import static br.com.dio.enuns.GameStatusEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;
    private Space space;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus(){
        if(spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return NON_STARTED;
        }

        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors(){
        if(getStatus() == NON_STARTED){
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }


    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished(){
        return hasErrors() && getStatus().equals(COMPLETE);
    }



}
