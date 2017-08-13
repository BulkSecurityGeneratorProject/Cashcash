CREATE TRIGGER trigger_cash_csv_config_prevent_user_id_update
BEFORE UPDATE ON cash_csv_config
FOR EACH ROW
  BEGIN
    IF (NEW.user_id != OLD.user_id)
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Update of user_id not allowed';
    END IF;
  END;
$$
CREATE TRIGGER trigger_cash_split_sum_prevent_user_id_update
BEFORE UPDATE ON cash_split_sum
FOR EACH ROW
  BEGIN
    IF (NEW.user_id != OLD.user_id)
    THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Update of user_id not allowed';
    END IF;
  END;
$$
