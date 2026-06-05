
function section_start () {
    local section_title="${1}"
    shift

    echo -e "\e[0Ksection_start:`date +%s`:${section_title}[collapsed=true]\r\e[0K""$@"
}

# Function for ending the section
function section_end () {
    local section_title="${1}"

    echo -e "\e[0Ksection_end:`date +%s`:${section_title}\r\e[0K"
}

function stage_enabled () {
    echo -e "stage ${1}:\t \e[32menabled\e[0m"
}

function stage_disabled () {
    echo -e "stage ${1}:\t \e[33mdisabled\e[0m"
}

function stage_success () {
    echo -e "stage ${1}:\t \e[32msuccess\e[0m"
}

function stage_failed () {
    echo -e "stage ${1}:\t \e[31mfailed\e[0m"
}

function stage_skipped () {
    echo -e "stage ${1}:\t \e[33mskipped\e[0m"
}