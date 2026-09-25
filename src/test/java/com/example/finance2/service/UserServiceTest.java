package com.example.finance2.service;

import com.example.finance2.exception.InsufficientFundsException;
import com.example.finance2.exception.InvalidAmountException;
import com.example.finance2.model.Role;
import com.example.finance2.model.Transaction;
import com.example.finance2.model.TransactionType;
import com.example.finance2.model.User;
import com.example.finance2.repository.TransactionRepository;
import com.example.finance2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UserService userService;

    private User darin;

    @BeforeEach
    void setUp() {
        darin = new User("Darin", "hash", new BigDecimal("100.00"), Role.USER);
        ReflectionTestUtils.setField(darin, "id", 1L);
    }

    @Test
    void depositIncreasesBalanceAndRecordsTransaction() {
        when(userRepository.findByName("Darin")).thenReturn(Optional.of(darin));

        userService.deposit("Darin", new BigDecimal("50.00"));

        verify(userRepository).increaseBalance(1L, new BigDecimal("50.00"));
        Transaction saved = capturedTransaction();
        assertThat(saved.getUserId()).isEqualTo(1L);
        assertThat(saved.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(saved.getAmount()).isEqualByComparingTo("50.00");
    }

    @Test
    void withdrawWithSufficientFundsRecordsWithdrawal() {
        when(userRepository.findByName("Darin")).thenReturn(Optional.of(darin));
        when(userRepository.decreaseBalanceIfSufficient(1L, new BigDecimal("30.00"))).thenReturn(1);

        userService.withdraw("Darin", new BigDecimal("30.00"));

        Transaction saved = capturedTransaction();
        assertThat(saved.getType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(saved.getAmount()).isEqualByComparingTo("30.00");
    }

    @Test
    void withdrawWithInsufficientFundsThrowsAndRecordsNothing() {
        when(userRepository.findByName("Darin")).thenReturn(Optional.of(darin));
        when(userRepository.decreaseBalanceIfSufficient(1L, new BigDecimal("500.00"))).thenReturn(0);

        assertThatThrownBy(() -> userService.withdraw("Darin", new BigDecimal("500.00")))
                .isInstanceOf(InsufficientFundsException.class);
        verify(transactionRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-0.01", "0.001", "1000000.01"})
    void rejectsInvalidAmountsWithoutTouchingTheDatabase(String amount) {
        assertThatThrownBy(() -> userService.deposit("Darin", new BigDecimal(amount)))
                .isInstanceOf(InvalidAmountException.class);
        assertThatThrownBy(() -> userService.withdraw("Darin", new BigDecimal(amount)))
                .isInstanceOf(InvalidAmountException.class);
        verifyNoInteractions(userRepository, transactionRepository);
    }

    @Test
    void rejectsMissingAmount() {
        assertThatThrownBy(() -> userService.deposit("Darin", null))
                .isInstanceOf(InvalidAmountException.class);
        verifyNoInteractions(userRepository, transactionRepository);
    }

    @Test
    void acceptsTrailingZerosBeyondTwoDecimals() {
        when(userRepository.findByName("Darin")).thenReturn(Optional.of(darin));

        userService.deposit("Darin", new BigDecimal("10.500"));

        verify(userRepository).increaseBalance(1L, new BigDecimal("10.500"));
    }

    private Transaction capturedTransaction() {
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        return captor.getValue();
    }
}
