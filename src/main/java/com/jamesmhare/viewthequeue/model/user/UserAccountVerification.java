package com.jamesmhare.viewthequeue.model.user;

import com.jamesmhare.viewthequeue.model.AbstractTimestampedEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a token that will be used to allow the User to verify their account.
 *
 * @author James Hare
 */
@Generated
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_account_verification")
public class UserAccountVerification extends AbstractTimestampedEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID token;

    @Column(nullable = false)
    private Date expiryDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_account_verification_to_user",
            joinColumns = @JoinColumn(name = "token"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

}
