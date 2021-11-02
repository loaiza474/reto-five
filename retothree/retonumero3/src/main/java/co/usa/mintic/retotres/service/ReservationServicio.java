package co.usa.mintic.retotres.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.usa.mintic.retotres.model.Reservation;
import co.usa.mintic.retotres.reportes.*;
import co.usa.mintic.retotres.repository.ReservationRepositorio;



@Service
public class ReservationServicio {
    @Autowired
    private ReservationRepositorio reservationRepositorio;

    public List<Reservation> getAll(){
        return reservationRepositorio.getAll();
    }

    public Optional<Reservation> getMensaje(int id){
        return reservationRepositorio.getMensaje(id);
    }
    
    
    public Reservation save(Reservation reserva){
        if(reserva.getIdReservation()==null){
            return reservationRepositorio.save(reserva);
        }else{
            Optional<Reservation> consulta=reservationRepositorio.getMensaje(reserva.getIdReservation());
            if(consulta.isEmpty()){
                return reservationRepositorio.save(reserva);
            }else{
                return reserva;
            }
        }
    }
    
    public boolean deleteReservation(int numId){

        Optional<Reservation> consulta=reservationRepositorio.getMensaje(numId);

        if(!consulta.isEmpty()){

            reservationRepositorio.delete(consulta.get());
            
            return true;
        }
        return false;
    }


    public Reservation update(Reservation reservation){
        if(reservation.getIdReservation()!=null){
            Optional<Reservation> consulta=reservationRepositorio.getMensaje(reservation.getIdReservation());
            if(!consulta.isEmpty()){
                if(reservation.getStartDate()!=null){
                    consulta.get().setStartDate(reservation.getStartDate());
                }
            if(reservation.getDevolutionDate()!=null){
                consulta.get().setDevolutionDate(reservation.getDevolutionDate());
                }
            if(reservation.getStatus()!=null){
                consulta.get().setStatus(reservation.getStatus());
                }
            return reservationRepositorio.save(consulta.get());
            }
        }
        return reservation;
    }


    public StatusReservas getReservationsStatusReport(){
        List<Reservation>completed=reservationRepositorio.getReservationByStatus("completed");
        List<Reservation>cancelled=reservationRepositorio.getReservationByStatus("cancelled");
    return new StatusReservas(completed.size(), cancelled.size());
    }
    
    public List<Reservation> getReservationPeriod(String dateA, String dateB){
        SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
        Date aDate= new Date();
        Date bDate= new Date();
        
       try {
           aDate = parser.parse(dateA);
           bDate = parser.parse(dateB);
       }catch(ParseException evt){
           evt.printStackTrace();
       }
       if(aDate.before(bDate)){
           return reservationRepositorio.getReservationPeriod(aDate, bDate);
       }else{
           return new ArrayList<>();
       } 
    
    }
    
    public List<ContadorClientes> getTopClients(){
        return reservationRepositorio.getTopClients();
    }
}
