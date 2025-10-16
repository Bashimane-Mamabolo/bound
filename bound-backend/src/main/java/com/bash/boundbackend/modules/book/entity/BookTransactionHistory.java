package com.bash.boundbackend.modules.book.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BookTransactionHistory extends AuditableEntity {

    // TODO - User relationship
    // TODO - Book relationship

    private boolean bookReturned;
    private boolean bookOwnerReturnApproved;


}
