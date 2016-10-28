﻿/*  This code is part of the Behavior Flow Query Language based on SWI-Prolog.

    Author:        Gregor Mehlmann
    E-mail:        mehlmann@hcm-lab.de
    WWW:           http://www.hcm-lab.de

    Copyright (C): 2008-2018,
                   University of Augsburg
                   Applied Computer Sciences
                   Human Centered Multimedia

    This program is free software; you can redistribute it and/or modify it
    under the terms of the 'GNU General Public License' as published by the
    Free Software Foundation, version 2 or any later version of the License.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY and without even the implied warranty
        of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

    You should have received a copy of the GNU General Public License along
    with this library; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

    As an exception, if you link this library with other files, compiled with
    a Free Software compiler, to produce an executable, this library does not
    by itself cause the resulting executable to be covered by the GNU General
    Public License. This exception does not invalidate any other reasons why
    the executable file might be covered by the 'GNU General Public License'.
*/
:- module(logic,
  [
    % Facts
    fsr/1,
    now/1,
    set/2,
    val/3,
    add/5,
    del/1,
    jel/1,
    add/1,
    jdd/1,
    rll/2,
    jll/2,
    % Print
    out/1,
    err/1,
    jog/1,
    % Timeout
    timeout/2,
    % Signals
    s/3,
    c/3,
    signal/3,
    consume/3,
    %signal/2,
    %consume/2,
    % Events
    gaze/1,
    move/3,
    voice/3,
    state/3,
    speech/1,
    % Garbage
    garbage/2,
    cleanup/0,
    % Variables
    val/2
  ]).

:- reexport('facts').
:- reexport('print').

%set(P, R, V) :-
%  fsr(R), set(P, V, R, S), add(S), del(R).

/*----------------------------------------------------------------------------*
 * Garbage Predicates
 *----------------------------------------------------------------------------*/
garbage(M, P) :-
  forall((fsr(R),
     val(mode, M, R),
     val(time, T, R),
     val(dist, D, R),
     val(life, L, R),
     E is T - D + L, now(Q),
     B is Q - P, B > E),
  jel(R)).
  
/*----------------------------------------------------------------------------*
 * Cleanup Predicates
 *----------------------------------------------------------------------------*/
cleanup :-
  write('Fact Base Cleanup'), nl,
  retractall(tmt(_,_)),
  retractall(now(_)),
  assertz(now(0)),
  rll(type, event),
  rll(type, signal).

/*----------------------------------------------------------------------------*
 * Unification Predicates
 *----------------------------------------------------------------------------*/
 val(X, Y) :-
   nonvar(X),
   nonvar(Y),
   X == Y.
 
/*----------------------------------------------------------------------------*
 * Timeout Predicates
 *----------------------------------------------------------------------------*/
%timeout(S, T) :- now(N), N > S + T.

:- dynamic tmt/2.

timeout(I, T) :-
  tmt(I, S), now(N), !,
  N > S + T,
  retractall(tmt(I, S)).
timeout(I, _) :-
  now(N), !,
  assertz(tmt(I, N)), fail.

/*----------------------------------------------------------------------------*
 * Signalling Predicates
 *----------------------------------------------------------------------------*/
 %ToDo: Make a signal/4 with an identifier
s(Process, Message, Content) :-
  signal(Process, Message, Content).
signal(Process, Message, Content) :-
  forall(
    (fsr(Record),
     val(type, signal, Record),
     %val(name, Message, Record),
     val(recv, Process, Record)),
  jel(Record)), now(Timestamp),
  jdd(
  [type:signal,
   recv:Process,
   name:Message,
   data:Content,
   time:Timestamp]).


c(Process, Message, Content) :-
  consume(Process, Message, Content).
consume(Process, Message, Content) :-
  fsr(Record),
  val(type, signal, Record),
  val(recv, Process, Record),
  val(name, Message, Record),
  val(data, Content, Record),
  jel(Record).

/*----------------------------------------------------------------------------*
 * Voice Event Extraction
 *----------------------------------------------------------------------------*/
% This predicate finds the oldest voice event based on the end times of the
% events and sets the name, the data and the event itself before deleting it.
voice(Name, Data, Event) :-
  findall(Record,
    (fsr(Record),
     val(type, event, Record),
     val(mode, voice, Record)),
    List),
  % Get oldest based on end times
  eoldest(Event, List),
  val(name, Name, Event),
  val(data, Data, Event),
  jel(Event).
/*----------------------------------------------------------------------------*
 * State Event Extraction
 *----------------------------------------------------------------------------*/
state(Name, Data, Event) :-
  findall(Record,
    (fsr(Record),
     val(type, event, Record),
     val(mode, state, Record)),
    List),
  eoldest(Event, List),
  val(name, Name, Event),
  val(data, Data, Event),
  jel(Event).
 /*----------------------------------------------------------------------------*
 * Gaze Event Extraction
 *----------------------------------------------------------------------------*/
gaze(Event) :-
  findall(Record,
    (fsr(Record),
     val(type, event, Record),
     val(mode, gaze, Record)),
    List),
   eoldest(Event, List),
   del(Event).

/*----------------------------------------------------------------------------*
 * Move Event Extraction
 *----------------------------------------------------------------------------*/
move(N, D, E) :-
  findall(R,
    (fsr(R),
     val(type, event, R),
     val(mode, move, R),
     val(name, N, R)), L),
  latest(E, L),
  val(data, D, E),
  forall(
    (fsr(R),
     member(R, L)),
     jel(R)).

     
/*----------------------------------------------------------------------------*
 * Speech Event Extraction
 *----------------------------------------------------------------------------*/
speech(Event) :-
  findall(R,
    (fsr(R),
     type(R, event),
     mode(R, speech)),
  List),
  latest(Event, List).

function(Function, Event) :-
  fsr(Event),
  val(data:data:data:function, Function, Event).
  
content(Content, Event) :-
  fsr(Event),
  val(data:data:data:content, Content, Event).

/*----------------------------------------------------------------------------*
 * Event Counting Predicates
 *----------------------------------------------------------------------------*/
count(Mode, Count) :-
  findall(Event, (fsr(Event),
     mode(Event, Mode)), List),
  length(List, Count).


/*----------------------------------------------------------------------------*
 * Time/Ordering Predicates
 *----------------------------------------------------------------------------*/
inewest(R, [R]) :- !.
inewest(R, [H|T]) :-
   inewest(L, T),
    (
      iafter(L, H), !, R = L
    ;
      iafter(H, L), !, R = H
    ).

iafter(A, B) :-
  val(time, TA, A),
  val(time, TB, B),
  val(from, DA, A),
  val(from, DB, B),
  val(life, LB, B),
  SA = TA - DA,
  SB = TB - DB,
  EB = SB + LB,
  SA > EB.

eoldest(R, [R]) :- !.
eoldest(R, [H|T]) :-
   eoldest(L, T),
    (
      ebefore(L, H), !, R = L
    ;
      ebefore(H, L), !, R = H
    ).
    
ebefore(A, B) :-
  val(time, TA, A),
  val(time, TB, B),
  val(from, DA, A),
  val(from, DB, B),
  val(life, LA, A),
  val(life, LB, B),
  EA = TA - DA + LA,
  EB = TB - DB + LB,
  EA =< EB.

test(R) :-
add([
    type:event,
    name:agent,
    mode:gaze,
    data:user,
    time:16010,
    from:0,
    life:0,
    conf:1.0
]),
add([
    type:event,
    name:user,
    mode:gaze,
    data:agent,
    time:16010,
    from:0,
    life:0,
    conf:1.0
]),gaze(R).