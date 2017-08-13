CREATE TRIGGER trigger_cash_currency_prevent_user_id_update
BEFORE UPDATE ON cash_currency
FOR EACH ROW
  BEGIN
    IF (NEW.user_id != OLD.user_id)
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Update of user_id not allowed';
    END IF;
  END;
$$
CREATE TRIGGER trigger_cash_account_prevent_user_id_update
BEFORE UPDATE ON cash_account
FOR EACH ROW
  BEGIN
    IF (NEW.user_id != OLD.user_id)
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Update of user_id not allowed';
    END IF;
  END;
$$
CREATE TRIGGER trigger_cash_split_prevent_user_id_update
BEFORE UPDATE ON cash_split
FOR EACH ROW
  BEGIN
    IF (NEW.user_id != OLD.user_id)
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Update of user_id not allowed';
    END IF;
  END;
$$
CREATE TRIGGER trigger_cash_transaction_prevent_user_id_update
BEFORE UPDATE ON cash_transaction
FOR EACH ROW
  BEGIN
    IF (NEW.user_id != OLD.user_id)
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Update of user_id not allowed';
    END IF;
  END;
$$