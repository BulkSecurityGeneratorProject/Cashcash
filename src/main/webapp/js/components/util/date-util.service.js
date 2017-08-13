export default function DateUtils($filter) {
    "ngInject";

    function convertDateTimeFromServer(date) {
        if (date) {
            return new Date(date);
        } else {
            return null;
        }
    }

    function convertLocalDateFromServer(date) {
        if (date) {
            var dateString = date.split('-');
            return new Date(dateString[0], dateString[1] - 1, dateString[2]);
        }
        return null;
    }

    function convertLocalDateToServer(date) {
        if (date) {
            return $filter('date')(date, 'yyyy-MM-dd');
        } else {
            return null;
        }
    }

    function dateformat() {
        return 'yyyy-MM-dd';
    }

    var service = {
        convertDateTimeFromServer: convertDateTimeFromServer,
        convertLocalDateFromServer: convertLocalDateFromServer,
        convertLocalDateToServer: convertLocalDateToServer,
        dateformat: dateformat
    };

    return service;
};
