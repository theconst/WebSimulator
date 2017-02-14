/* 
 * simple script to list students
 */
$(window).on('load', function () {
    
    $('#list').click(function () {
        getListOfStutends(function (studentList) {
            /* clear table */
            $('#student-list').html($('#student-list-header').html()).append(
                    toTable(studentList, ['login', 'name', 'group', 'variant']));
        }, function (_, status, _) {
            console.log('Response status: ' + status);
        });
    });
    
    function getListOfStutends(success, failure) {
        $.ajax({
            url: '/WebSimulator/liststudents',
            method: 'GET',
            async: true,
            cache: false,
            dataType: 'json',
            error: failure,
            success: success
        });
    }

    /**
     * Creates table from object
     * 
     * @param {object} obj object 
     * @param {array} props properties included in the table
     * @returns {string} contents of object in table form
     */
    function toTable(obj, props) {

        /* Simple helper functions */
        function entry(e) {
            return '<td>' + e + '</td>';
        }

        function row(r) {
            return '<tr>' + r + '</tr>';
        }

        /* map to html table */
        return obj.map(function (field) {
            return row(props.map(function (prop) {
                return entry(field[prop]);
            }));
        }).join();
    }
});


