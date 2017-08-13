export default function capitalize() {
    function capitalizeFilter(input) {
        if (input !== null) {
            input = input.toLowerCase();
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    return capitalizeFilter;
};