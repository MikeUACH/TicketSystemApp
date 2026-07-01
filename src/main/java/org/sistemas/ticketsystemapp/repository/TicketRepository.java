package org.sistemas.ticketsystemapp.repository;


import java.util.List;
import org.sistemas.ticketsystemapp.entity.Ticket;
import org.sistemas.ticketsystemapp.entity.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findBySessionTokenAndStatusNotIn(
            String sessionToken,
            List<TicketStatus> statuses
    );

    Page<Ticket> findByStatusContainingAndPriorityContainingAndCategoryContaining(
            String status,
            String priority,
            String category,
            Pageable pageable
    );

    long countByStatus(TicketStatus status);
}

